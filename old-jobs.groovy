import groovy.time.TimeCategory

use ( TimeCategory ) {
  yeareago = (new Date() - 12.months)
}

jobs = Jenkins.instance.getItems()
oldJobsCount = 0

jobs.each { job ->
    
  lastBuild = job.getLastBuild()
  if (lastBuild != null && lastBuild.getTime() < yeareago) {
    oldJobsCount++
    println 'http://machydra.brq.redhat.com:8080/' + job.getUrl()
  }

}
println oldJobsCount + ' old jobs found!'
