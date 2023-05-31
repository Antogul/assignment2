import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class Client{
    private static final int PORT = 50000;
    private static final String SERVER_ADDRESS = "localhost";
    private static Socket socket;
    private static BufferedReader dint;
    private static DataOutputStream dout;

    public Client(String[] args){
        //createa a socket
        socket = new Socket(SERVER_ADDRESS, PORT);

        //initialize input and output
        dint = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        dout = new DataOutputStream(socket.getOutputStream());

        //Helo
        dout.write(("HELO\n").getBytes());
        dout.flush();
        System.out.println("Send: Helo");

        String line = dint.readLine();
        System.out.println("RCVD: "+line) ;

        //auth

        dout.write(("AUTH Antranig\n").getBytes());
        dout.flush();
        System.out.println("Send: AUTH");
        line = dint.readLine();
        System.out.println("ECD:  "+line) ;
        
            
            
    }

}

class Job {
    private int jobId;
    private int jobSize;
    private int priority;
    

    public Job(int jobId, int jobSize, int priority) {
        this.jobId = jobId;
        this.jobSize = jobSize;
        this.priority = priority;
    }

    public int getJobId() {
        return jobId;
    }

    public int getJobSize() {
        return jobSize;
    }

    public int getPriority() {
        return priority;
    }
}

class Server {
    private int serverId;
    private int availability;

    public Server(int serverId, int availability) {
        this.serverId = serverId;
        this.availability = availability;
    }

    public int getServerId() {
        return serverId;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }
}

public class WeightedTurnaroundTimeScheduling {
    public static void scheduleJobs(List<Job> jobs, List<Server> servers) {
        // Sort jobs based on weighted turnaround time (WTT)
        Collections.sort(jobs, Comparator.comparingInt(j -> (j.getPriority() * (j.getJobSize()))));

        for (Job job : jobs) {
            boolean jobScheduled = false;
            for (Server server : servers) {
                if (server.getAvailability() >= job.getJobSize()) {
                    System.out.println("Job " + job.getJobId() + " scheduled on Server " + server.getServerId());
                    server.setAvailability(server.getAvailability() - job.getJobSize());
                    jobScheduled = true;
                    break;
                }
            }
            if (!jobScheduled) {
                System.out.println("Job " + job.getJobId() + " could not be scheduled.");
            }
        }
    }

    public static void main(String[] args) {
        // Create job list
        List<Job> jobs = new ArrayList<>();
        jobs.add(new Job(1, 5, 1));
        jobs.add(new Job(2, 7, 2));
        jobs.add(new Job(3, 3, 3));
        jobs.add(new Job(4, 4, 4));
        jobs.add(new Job(5, 6, 5));

        // Create server list
        List<Server> servers = new ArrayList<>();
        servers.add(new Server(1, 10));
        servers.add(new Server(2, 5));
        servers.add(new Server(3, 8));

        // Schedule jobs
        scheduleJobs(jobs, servers);

        // close connection with server
        dout.write(("QUIT\n").getBytes());   
        dout.flush();
        System.out.println("Send: QUIT");

        line = dint.readLine();
        System.out.println("RCVD: "+line) ;

        dint.close();
        dout.close();
        socket.close();
    }
}
