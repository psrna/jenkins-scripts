jobs = Jenkins.instance.getItems()
String[] owners = ["psrna", "vpakan", "ljelinko", "mmalina", "rhopp", "rawagner", "mlabuda", "apodhrad", "jpeterka", "jrichter"]

jobsCount = 0
jobs.each { job ->
    
  description = job.getDescription()

  if (description == null) {
    //no description - report
    println 'http://machydra.brq.redhat.com:8080/' + job.getUrl()
    jobsCount++
    return
  }
  if (!description.contains('@')){
    //if no email, check owner
    ownerFound = false
    owners.each { o ->
    	if(description.contains(o)){
        	ownerFound = true
        }else if(job.getUrl().contains(o)){
         	ownerFound = true 
        }
    }
    if(!ownerFound){
    	//report
      	println 'http://machydra.brq.redhat.com:8080/' + job.getUrl()
      	jobsCount++
  	}
    
  }

}
println  jobsCount + ' jobs without proper description found!'
