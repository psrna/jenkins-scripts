jobs =  Jenkins.instance.getItems(Job.class)
oldJobsCount = 0

jobs.each { job ->
    firstBuild = job.getFirstBuild()
    if (firstBuild == null) {
        oldJobsCount++
        println 'https://dev-platform-jenkins.rhev-ci-vms.eng.rdu2.redhat.com/' + job.getUrl()
    }
}
println oldJobsCount + ' never run jobs found!'
