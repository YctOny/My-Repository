package com.company;

import BasicIO.*;                // for IO classes
import static BasicIO.Formats.*; // for field formats
import static java.lang.Math.*;  // for math constants and functions


/** This class runs a population census program and generate a table of result.
 *
 * @author Yuchen Tong
 * @version 1.0 (November 29 2016)                                                        */

class DataCollection {

    ASCIIDataFile input;     //The input file which stores the household address.
    ASCIIOutputFile output;  //The output file as a census table.
    BasicForm houseHold;     //The form which processes the number of residents.
    BasicForm individual;    //The form which processes the information of individual.

    String address;          //Household address.
    int totalNum;            //Total number of person per house.
    int maleNum;             //Number of males per house.
    int femaleNum;           //Number of females per house.
    int engSpker;            //Number of English speakers per house.
    int frenSpker;           //Number of French speakers per house.
    int otherSpker;          //Number of other language speakers per house.

// instance variables


    /** This constructor processes the household window and individual window
     * for census takers to write information. At last, it generates a table of
     * population census per household.
     */
    public DataCollection() {

        input = new ASCIIDataFile();
        output = new ASCIIOutputFile();

        setUpHousehold();
        setUpIndividual();

        for ( ; ; ) {
            address = input.readString();

            if (input.isEOF()) break;

            processHousehold();
            processIndividual();

            output.writeString(address);
            output.writeInt(totalNum);
            output.writeInt(maleNum);
            output.writeInt(femaleNum);
            output.writeInt(engSpker);
            output.writeInt(frenSpker);
            output.writeInt(otherSpker);
            output.newLine();
        }

        input.close();
        output.close();
        houseHold.close();
        individual.close();

        System.exit(0);
        // statements

    }
    // constructor

    /** This method processes the household window in order to
     * get the number of people per house.
     * */
    private void processHousehold() {
        int controlNum;    // The number which will be returned by accept()

        houseHold.clearAll();
        updateAddress();
        controlNum = houseHold.accept();
        houseHold.hide();
        switch (controlNum) {
            case 0:
                totalNum = houseHold.readInt("NumPeople");
                break;
            case 1:
                totalNum = 0;
                clearInstance();
                break;
        }
    }//processHousehold

    /** This method initialize the Number of male, female, English speaker,
     * French speaker and other language speakers to 0(Except Total number of people)
     * In order to update the variable each time the individual window processes.
     * */
    private void clearInstance() {
        maleNum = 0;
        femaleNum = 0;
        engSpker = 0;
        frenSpker = 0;
        otherSpker = 0;
    }//clearInstance

    /** This method process the individual window for census takers to write the
     * individual information.
     * */
    private void processIndividual() {
        int controlNum;    // The number which will be returned by accept()

        clearInstance();
        for(int i=1;i<=totalNum;i++) {
            individual.clearAll();
            updatePerson(i);

            controlNum = individual.accept();
            individual.hide();

            if(controlNum == 0) {
                int controlNum1 = individual.readInt("sex");      // The number which will be returned by accept()
                int controlNum2 = individual.readInt("language"); // The number which will be returned by accept()

                if(controlNum1 == 0)
                    maleNum++;
                else if(controlNum1 == 1)
                    femaleNum++;

                if(controlNum2 == 0)
                    engSpker++;
                else if(controlNum2 == 1)
                    frenSpker++;
                else if(controlNum2 == 2)
                    otherSpker++;
            }
        }
    }//processIndividual

    /** This method set up the form.
     * */
    private void setUpHousehold() {
        houseHold = new BasicForm("OK", "Skip");
        houseHold.setTitle("Household");
        houseHold.addTextField("address","Address",20);
        houseHold.setEditable("address",false);
        houseHold.addTextField("NumPeople", "# People",3);
    }//setUpHousehold

    /** This method updates the address which appears on the household form.
     */

    private void updateAddress() {
        houseHold.writeString("address",address);
    }//updateAddress

    /** This method updates the NO. people which appears on the individual form.
     * */
    private void updatePerson(int num) {
        individual.writeString("person",num+"");
    }//updatePerson

    /** This method sets up the individual form.
     * */
    private void setUpIndividual() {
        individual = new BasicForm("OK");
        individual.setTitle("Individual");
        individual.addTextField("person","Person",3);
        individual.setEditable("person",false);
        individual.addTextField("name","Name",10);
        individual.addTextField("age","Age",4);
        individual.addRadioButtons("sex","Sex",false,10,90,"Male","Female");
        individual.addRadioButtons("language","Language",false,10,140,"English","French","Other");
    }//setUpIndividual

    public static void main(String[] args) {DataCollection d = new DataCollection();}

    // methods

}//DataCollection


public class Main {

    public static void main(String[] args) {
	// write your code here
        DataCollection d = new DataCollection();
    }
}


