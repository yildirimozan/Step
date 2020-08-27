#include <AccelStepper.h>
#include <MultiStepper.h>
#include <A4988.h>
#include <BasicStepperDriver.h>
#include <DRV8825.h>
#include <MultiDriver.h>
#include <SyncDriver.h>
#include <Stepper.h>

#define motorInterfaceType 1
#define buzzer 3

Stepper Nema14(200, 2 , 5);

int hiz = 0 , stepnum =0;
int deger;
String gelen =""; 
int stepCount = 0;  
int aci =0;
int yon=1;

 
void setup() {
Serial.begin(9600);
pinMode(buzzer,OUTPUT);

/*Nema14.setSpeed(200);
Nema14.step(400);

Nema14.setSpeed(20);
Nema14.step(-200);
*/
digitalWrite (buzzer, HIGH);
  delay(200);
  digitalWrite (buzzer, LOW) ;

  
}

void loop() {

if(Serial.available()){
  
  gelen   = Serial.readString();
  hiz     = gelen.substring(1, 4).toInt();
  aci     = gelen.substring(4,7).toInt();
  stepnum = gelen.substring(7,10).toInt();
  /*stepnum =4*stepnum;*/
  
  digitalWrite (buzzer, HIGH);
  delay(100);
  digitalWrite (buzzer, LOW) ;


  Serial.println(gelen + ", Hiz = "+ hiz +" rpm  , " + "Aci = "+ aci +", Step = "+ stepnum + " .");
  
        if(gelen.substring(0,1) == "1")   // saat yönü tersindeyse bu satıra girecek (sağ)
          {
             yon  = 1;
           } 
           else if(gelen.substring(0,1) == "2")   // saat yönündeyse  bu satıra girecek (sol)
             {
               yon  = -1;
             }
               
              if(hiz>0&&stepnum>0&&aci==0) // hız değeri ve step değeri geldiyse  .
                {     
                   stepnum    = stepnum*yon;
                   Nema14.setSpeed(hiz);
                   Nema14.step(stepnum); 
                   stepCount  = stepCount +stepnum;
                }
                else if(hiz==0&&stepnum>0&&aci==0) // sadece step değeri gelirse sabit 50 lik bir hızla döner.
                  {
                    stepnum   = stepnum*yon;
                    hiz       = 50; 
                    Nema14.setSpeed(hiz);
                    Nema14.step(stepnum); 
                    stepCount = stepCount +stepnum; 
                  }
                  else if(hiz>0&&stepnum==0&&aci>0) // hız ve açı aynı anda geldiyse.
                  {
                    stepnum   = aci*2.22*yon; 
                    Nema14.setSpeed(hiz);
                    Nema14.step(stepnum); 
                    stepCount = stepCount +stepnum;                    
                  }
                  else if(hiz==0&&stepnum==0&&aci>0) // açı değeri sabit 50 lik hızla döner.
                  {
                    stepnum  = aci*2.22*yon;
                    hiz      = 50; 
                    Nema14.setSpeed(hiz);
                    Nema14.step(stepnum); 
                     
                  }
                  else if(hiz>0&&stepnum==0&&aci==0) // sadece hız gelirse 20 tur atar 
                  {
                    stepnum  = 800*20*yon;
                   
                    Nema14.setSpeed(hiz);
                    Nema14.step(stepnum);
               
                  }
             
           
  
     
  Serial.flush(); 
}


}
