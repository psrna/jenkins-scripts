import hudson.model.*
import hudson.triggers.*

jobs = Jenkins.instance.getItems()
jobs.each { job ->
    
  if(job instanceof com.cloudbees.hudson.plugins.folder.Folder) { return }
	
    job.triggers.each{ descriptor, trigger ->
        if(trigger instanceof TimerTrigger) {
            println("--- Timer trigger for " + job.name + " ---")
            println(trigger.spec + '\n')
        }
    }
}
