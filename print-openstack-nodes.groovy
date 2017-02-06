import hudson.slaves.Cloud;
import jenkins.plugins.openstack.compute.*;
import org.openstack4j.model.compute.Server;

List<String> cloudNames = new ArrayList<>();
for (Cloud c : Jenkins.getActiveInstance().clouds) {
  
  //println(c)
  if (c instanceof JCloudsCloud) {
    final List<Server> runningNodes = c.getOpenstack().getRunningNodes();
    //println(runningNodes);
    //cloudNames.add(c.name);
    
    for(Server node : runningNodes){
     	println node.getName() 
    }
  }
}

