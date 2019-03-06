import groovy.time.TimeCategory

use ( TimeCategory ) {
  yeareago = (new Date() - 12.months)
}

jobs = Jenkins.instance.getItems(Job.class)
oldJobsCount = 0

jobs.each { job ->
    lastBuild = job.getLastBuild()
    if (lastBuild != null && lastBuild.getTime() < yeareago) {
        oldJobsCount++
        println 'https://dev-platform-jenkins.rhev-ci-vms.eng.rdu2.redhat.com/' + job.getUrl()
    }
}
println oldJobsCount + ' old jobs found!'
