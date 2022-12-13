import java.io.*;
import java.util.*;


// Main class should be named 'Solution'
public class Solution {

    DataCenter dataCenter = new DataCenter();
    public static void main(String[] args) {
        System.out.println("Hello, World");

    }
}
 class Resource {
    private String type;
    private int cpu;
    private double price;

    public Resource(String type, int cpu, double price) {
        this.type = type;
        this.cpu = cpu;
        this.price = price;
    }

    public String getType() {
        return this.type;
    }

    public int getCpu() {
        return this.cpu;
    }

    public double getPrice() {
        return this.price;
    }
}

 class DataCenter {

     Queue<Task> waitingQueue = new LinkedList<>();
    private Hashtable<String, List<Resource>> availableResources;
    private Hashtable<String, List<Resource>> allocatedResources;

    public void addResource(Resource resource) {
        String type = resource.getType();
        if (!this.availableResources.containsKey(type)) {
            this.availableResources.put(type, new ArrayList<Resource>());
        }
        this.availableResources.get(type).add(resource);
    }

    public void removeResource(Resource resource) throws Exception {
        String type = resource.getType();
        if (!this.availableResources.containsKey(type)) {
                throw new Exception("Resource not found");
        }
        if(this.availableResources.get(type).contains(resource)){
            this.availableResources.get(type).remove(resource);
        }
        if( this.allocatedResources.containsKey(type) && this.allocatedResources.get(type).contains(resource)){
           throw new Exception("Resource is allocated");
        }
    }

    public List<Resource> getAvailableResources(String type, int minCpu) {
        if (!this.availableResources.containsKey(type)) {
            return null;
        }
        List<Resource> resources = this.availableResources.get(type);
        List<Resource> filteredResources = new ArrayList<>();
        for (Resource resource : resources) {
            if (resource.getCpu() >= minCpu) {
                filteredResources.add(resource);
            }
        }
        return filteredResources;
    }

    public List<Resource> getAllocatedResources(String type) {
        if (!this.allocatedResources.containsKey(type)) {
            return null;
        }
        return this.allocatedResources.get(type);
    }

    public void allocateResources(List<Resource> resources, Task task) {
        Criteria criteria = task.getCriteria();
        int minCpu = task.getMinCpu();

        for (Resource resource : resources) {
            String type = resource.getType();
            // check for available resources based on criteria
            if (criteria.equals(Criteria.HighCPU) && resource.getCpu() > minCpu) {
                if (!this.allocatedResources.containsKey(type)) {
                    this.allocatedResources.put(type, new ArrayList<Resource>());
                    task.status = TaskStatus.RUNNING;
                }
                this.allocatedResources.get(type).add(resource);
                task.status = TaskStatus.RUNNING;
            } else if (criteria.equals(Criteria.LowPrice) && resource.getPrice() == minCpu) {
                if (!this.allocatedResources.containsKey(type)) {
                    this.allocatedResources.put(type, new ArrayList<Resource>());
                    task.status = TaskStatus.RUNNING;
                }
                this.allocatedResources.get(type).add(resource);
                task.status = TaskStatus.RUNNING;
            }
            //if no resource is allocated put task on waitlist
            if (!this.allocatedResources.containsKey(type)) {
                task.status = TaskStatus.PENDING;
                waitingQueue.add(task);
            }
        }
    }
    public void deallocateResources(List<Resource> resources) {
        for (Resource resource : resources) {
            String type = resource.getType();
            this.allocatedResources.get(type).remove(resource);
            if (!this.availableResources.containsKey(type)) {
                this.availableResources.put(type, new ArrayList<Resource>());
            }
            this.availableResources.get(type).add(resource);
        }
    }

    //poll the waitingQueue and allocate resources to the task
    public void allocateWaitingQueue(){
        while(!waitingQueue.isEmpty()){
            Task task = waitingQueue.poll();
            List<Resource> resources = getAvailableResources(task.getTaskType(), task.getMinCpu());
            allocateResources(resources, task);
        }
    }
}
enum TaskStatus{
    CREATED,PENDING, RUNNING, COMPLETED
}

enum Criteria{
    HighCPU, LowPrice;
}
 class Task{
    long start_time;
    long end_time;
    String taskType;
    Criteria criteria;
    int minCpu;

    TaskStatus status;

     public long getStart_time() {
         return start_time;
     }

     public long getEnd_time() {
         return end_time;
     }

     public String getTaskType() {
         return taskType;
     }

     public Criteria getCriteria() {
         return criteria;
     }

     public int getMinCpu() {
         return minCpu;
     }

     //create a queue of tasks and add them to the queue
    public Task(String taskType, int minCpu, Criteria criteria){
        this.taskType = taskType;
        this.minCpu = minCpu;
        this.criteria  = criteria;
        this.taskType = taskType;
        this.status = TaskStatus.CREATED;
    }

    //get the task status
     public TaskStatus getTaskStatus() {
         return this.status;
     }


 }
