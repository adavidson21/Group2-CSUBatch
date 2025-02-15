import java.util.Scanner;
public class UI {
    private Scanner userInput = new Scanner(System.in);
    public void generateUI(){
        System.out.println("Welcome to the CSUBatch Scheduling Application");
        System.out.println("Thank you for downloading.");
        System.out.println("This System is meant to act as a scheduling application where jobs can be added to a queue that will be arranged based \n on the selected priority. \n commands: run, list, policy_change, help, exit");
        userInteraction();
    }
    public void userInteraction(){
        System.out.println("Please Enter Command: ");
        String command = userInput.nextLine();
        while(!"exit".equals(command)){
            if(command.equals("run")){
                //add in the run operations
            }
            if(command.equals("list")){
                //add in list operations
            }
            if(command.equals("policy_change")){
                //change policy
            }
            if(command.equals("help")){
                //enter help operations
            }
            else{
                System.out.println("Sorry command unrecognized try again");
            }
            command = userInput.nextLine();
        }
    }
}
