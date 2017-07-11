def branches = [:]
def matrix = "${MATRIX}".toString().split('\n')

void provisionTestDeploy(String snapshotName, String labels, String baseImage, String machineFlavor){
    
    sh """
        function clean_openstack {
	        ${WORKSPACE}/bin/runscript.groovy \"client.compute().servers().delete(\\"\$SERVER_ID\\").isSuccess()\"
            floatingIpId=\$($WORKSPACE/bin/runscript.groovy 'client.compute().floatingIps().list().find{ it.floatingIpAddress.equals(\"'\$MACHINE_IP'\") }.each { println \"\${it.id}\"}')
            ${WORKSPACE}/bin/runscript.groovy \"client.compute().floatingIps().deallocateIP(\\"\$floatingIpId\\")\"
        }

        PATH=$PATH:${COMMON_TOOLS}${SEP}groovy-2.4.3/bin

        export OS_KEY_NAME=rhopp_key
        export MACHINE_NETWORKS=\"dev-platform-jenkins-network\"
        export MACHINE_FLAVOR=${machineFlavor}

        . ${WORKSPACE}/bin/provision.sh ${snapshotName} ${baseImage} \"${labels}\" trap clean_openstack EXIT

        test_repo=\"git@gitlab.mw.lab.eng.bos.redhat.com:dev-platform/mwqa-env-suite.git\"
        test=\"git clone \$test_repo && ./mwqa-env-suite/run.sh ${labels}\"
        \$SSH hudson@\$MACHINE_IP \"\$test\"

        ${WORKSPACE}/bin/runscript.groovy ${WORKSPACE}/bin/update-snapshot.groovy --server \${SERVER_ID} --name ${snapshotName}
        clean_openstack
    """
}

//Generate stage for every snapshot
for(int i=0; i<matrix.length; i++){
    def param = matrix[i].split(',')
        
    def snapshotName = param[0]
    def labels = param[1]
    def baseImage = param[2]
    def machineFlavor = param[3]

    branches["branch_${snapshotName}"] = { 
        node('rhel7'){
            stage('Preparation') { // for display purposes
                // Get some code from a GitHub repository
                git url: "git@gitlab.mw.lab.eng.bos.redhat.com:${params.FORK}/mwqa-cloud-slaves.git", branch: params.BRANCH
                // Get the Maven tool.
                // ** NOTE: This 'maven3-latest' Maven tool must be configured
                // **       in the global configuration.           
                mvnHome = tool 'maven3-latest'
                configFileProvider([configFile(fileId: 'org.jenkinsci.plugins.configfiles.custom.CustomConfig1468399129856', variable: 'OPENRC_SH')]) {
                    // some block
                    sh "cp ${OPENRC_SH} ${WORKSPACE}/openrc.sh"
                }
            }
                
            stage("${snapshotName}"){
                provisionTestDeploy(snapshotName, labels, baseImage, machineFlavor)
            }
        }
    }
}

parallel branches
