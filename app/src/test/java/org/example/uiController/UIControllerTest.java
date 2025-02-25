package org.example.uiController;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UIControllerTest {
    private UIController UI = new UIController();
    @AfterAll
    public static void tearDown() {
        // clean up after all tests
    }
    @Test
    @DisplayName("Should successfully create the UI System and then move to take user input.")
    public void testGenerate() throws InterruptedException{
        //both generates the UI and then sends it into being used for user interaction.
        UI.generateUI();
    }
    @Test
    @DisplayName("Simulates the input function of the UI class with the various inputs.")
    public void testInputAcceptance(){
        int i = 0;
        System.out.println("Please Enter Command: ");
        String command = "run fishing 100 10";
        String[] commandarr = command.split(" "); //an array in place so the commands that are more than one word can be parsed.
        while(!"exit".equals(command)){
            if(commandarr[0].equals("run")){
                if(commandarr.length != 4 ){
                    System.out.println("Invalid run command please try again");
                    break;
                }
                else{
                    try { //Try in place to ensure that the user enters the run command format correctly andif not throws an error that does not crash the system.
                        String job_time = commandarr[2];
                        String job_priority = commandarr[3];
                        int job_time_int = Integer.parseInt(job_time);
                        int job_priority_int = Integer.parseInt(job_priority);
                        //add in queue functionality here Zac & Trenten.
                        System.out.println("Job: " + commandarr[1] + " added to queue");
                    } catch (Exception e) {
                        System.out.println("Sorry time and priority must be able to be converted to integer try again");
                    }
                }
            }
            else if(command.equals("list")){
                //add in list operations
            }
            else if(commandarr[0].equals("policy_change")){
                if(commandarr.length != 2){
                    System.out.println("invald policy_change command please try again");
                }
                else{
                    //Enter policy change functionality here.
                    System.out.println("policy change successful");
                }
            }
            else if(command.equals("help")){
                System.out.println("User has enter help");
                System.out.println("Command List:");
                System.out.println("run <job name> <job time> <priority> - Will add a job to the system");
                System.out.println("list - Print out the current job queue.");
                System.out.println("policy_change <policy> - will change the policy to the new entered one and restructure queue.");
                System.out.println("exit - End System processes and perform benchmark on close");
            }
            else{
                System.out.println("Sorry command unrecognized try again");
            }
            commandarr = null; //empties the array so new commands can be entered
            if(i == 0){
                command = "list";
                i++;
            }
            else if(i == 1){
                command = "help";
                i++;
            }
            else if(i == 2){
                command = "policy fcfs";
                i++;
            }
            else if(i == 3){
                command = "run fish hello ten"; //invalid run input test.
                i++;
            }
            else if(i == 4){
                command = "invalid input";
                i++; //invalid input test 
            }
            else{
                command = "exit";
            }
            commandarr = command.split(" "); //refills the array with new entries.
        }
        System.out.println("System ending...");
    }

}
