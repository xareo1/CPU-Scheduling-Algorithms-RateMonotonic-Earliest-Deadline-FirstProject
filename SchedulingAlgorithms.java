import java.util.Scanner;
import java.util.*;
import java.lang.Math;

public class Main
{
 public static void main(String[] args) 
 {
    Scanner sc =new Scanner(System.in);
    
    System.out.println("how many cpu task do you want to create?");
    int taskCount;
    taskCount = sc.nextInt();
    
     int[] task = new int[taskCount];//task number given by user
     int[] taskIndex = new int[taskCount];//task period icinde bittiginde 
     
     int[] executeTime = new int[taskCount];//execute time
     int[] executeComplete = new int[taskCount];
     int[] executeCounter = new int[taskCount];
     int[] executeFlag = new int[taskCount];
     int breakFlag=0;
     int nullFlag=0;
     
     int[] period = new int[taskCount];//hold duration of every task
     int[] priority = new int[taskCount];//shortest period get the higher priority
     int[] periodTemp = new int[taskCount];
     int counter=0;
     
     int choice;
     System.out.println("Which algorithm do you want to use? ");
     System.out.println("Rate Monotonic Scheduling 1, Earliest Deadline First icin 2");
     choice= sc.nextInt();
     
     if(choice==1)
     {
         System.out.println("Rate Monotonic Scheduling algorithm is selected");
         
         
         System.out.println("enter the duration and period of the tasks ");
    
    for(int i=0; i< (taskCount) ; i++)
    { 
      System.out.println("task "+ (i+1) + " enter total duration of task by seconds" ); 
      executeTime[i] = sc.nextInt();
      
      System.out.println("task "+ (i+1) + " enter period of task by seconds" );
      period[i] = sc.nextInt();
      priority[i]= period[i];
      periodTemp[i]=period[i];
    }
    
    //The task with lowest period will get higher priority, this is why tasks are sorted by low to high.
    Arrays.sort(priority);
    
    
    float Ui= 0;  //utilization of task, check for (exec/period)
    
    for(int UiCheck=0; UiCheck<taskCount ; UiCheck++)
    {
        Ui = Ui + (float)executeTime[UiCheck] / period[UiCheck] ;
    }
    System.out.println("Ui: " + Ui );
    
    double inverseTaskcount = 1/(double)taskCount;
    
    double bound;
    bound = taskCount * (Math.pow(2,inverseTaskcount) - 1 );
    
    int utilizationFlag=1;
   
    if(Ui>1)
    {
        System.out.println("Ui 1'den buyuk" );
        utilizationFlag=0;
    }
 
    //zamanı ilerleten for katmani, periodu en yuksek olan islemin iki katı kadar suruyor
    //for loop continues the time
    if(utilizationFlag!=0)
    {
        
        for(int timeStamp=0 ; timeStamp < priority[(taskCount-1)] ; )
     {
          if(timeStamp%priority[0]!=0)
          {
              for(int idlectr=0;idlectr<taskCount;idlectr++)
              {
                  if(executeFlag[idlectr]==1)
                  {
                      nullFlag++;
                  }    
              }
              
              if(nullFlag==taskCount)//3
                  {
                      System.out.print("Current time(second): " + timeStamp + " " );
                      System.out.println("processor is idle");
                      timeStamp++;
                      nullFlag=0;
                  }
                  else
                  {
                      nullFlag=0;
                  }
          }
          
          counter=0;
          for(int l=0;l<taskCount;l++)
          {
              if(period[l]==priority[counter])
              {
                  for(int j=0; j<priority[0];j++)
                  {
                      
                    if((taskIndex[l])< (timeStamp/period[l]))
                         {
                          System.out.println("deadline of task "+(l+1)+ "exceeded,given tasks are not possible to schedule ");
                          System.exit(1);
                         }  
                      
                    if(executeCounter[l]<executeTime[l] && executeFlag[l]!=1)
                    {
                     System.out.print("Current time(second): " + timeStamp + " " );
                     System.out.println("Task " + (l+1)+"-"+ taskIndex[l] +" is processing");
                     //System.out.println("counter: "+ counter + " executeCounter"+ executeCounter[l] );
                     timeStamp++;
                     executeCounter[l]++;
                     
                         if(executeTime[l]==executeCounter[l] )
                         {
                         executeCounter[l]=0;
                         taskIndex[l]++;
                         executeFlag[l]=1;
                         
                         //System.out.println("flag of Task " + (l+1)+" activated");
                         }//executeTime if end
                        
                        for(int resetter=0;resetter<taskCount;resetter++)
                         {
                            if((timeStamp)%period[resetter]==0)
                            {
                             executeFlag[resetter]=0;
                             //System.out.println("flag of Task " + (l+1)+" deactivated");
                            }
                         }
                         
                    }//executeCounter if end
                   
                    for(int resetter2=0;resetter2<taskCount;resetter2++)
                    {
                        if(executeFlag[resetter2]==0 && resetter2<l)
                        {
                            counter=0; 
                            breakFlag=1;
                            break;
                        }
                    }
                    
                    if(breakFlag==1)
                    {
                        breakFlag=0;
                        break;
                    }
                      
                  }//j for end
                  counter++;
                  if(counter%taskCount==0)
                  {
                    counter=0;  
                  }
                  
              }//priority if end
          }//l for end   
    }//timeStamp for end 
    }
         
     }//choice if end
     else if(choice==2)
     {
         System.out.println("Earliest Dead Line First Scheduling'e hosgeldiniz");  
         System.out.println("tasklarin calisma suresi ve deadline'lariin sirayla giriniz ");
    
    for(int i=0; i< (taskCount) ; i++)
    {
      //notasyona task 0 yazmamak icin kullaniciya task[0] i task 1 olarak gostericem    
      System.out.println("task "+ (i+1) + " icin calisma suresini giriniz" ); 
      executeTime[i] = sc.nextInt();
      
      
      System.out.println("task "+ (i+1) + " icin deadline giriniz" );
      period[i] = sc.nextInt(); // EDF icin fazladan degisken tanimlamak istemedim, period deadline olarak kullanilacak
      priority[i]= period[i];
      periodTemp[i]=period[i];
    }
    
    //deadline'i az olan daha oncelikli oluyor, kucukten buyuge sort yaptim boylelikle
    //en bastaki eleman en yuksek oncelikli islemi temsil edecek
    Arrays.sort(priority);
    
    
    float Ui= 0;  //utilization of task, exec/period // kontrol ediyoruz.
    
    for(int UiCheck=0; UiCheck<taskCount ; UiCheck++)
    {
        Ui = Ui + (float)executeTime[UiCheck] / period[UiCheck] ;
    }
    System.out.println("Ui: " + Ui );
    
    
    int utilizationFlag=1;
    
    if(Ui>1)
    {
        System.out.println("Ui 1'den buyuk" );
        utilizationFlag=0;
        System.exit(1);
    }
      
       for(int timeStamp=0 ; timeStamp < 30 ; )
        {
            
            for(int resetter=0;resetter<taskCount;resetter++)
            {
                if( timeStamp%period[resetter]==0 )
                    {
                        executeCounter[resetter]=0;
                    }
            }

         for(int l=0;l<taskCount;l++)
          {
           
           for(int k=0;k<taskCount;k++)
           {
               
               if( k!=l &&  periodTemp[l]<periodTemp[k] || k!=l &&  periodTemp[l]==periodTemp[k]  )
               { 
                if(executeCounter[l]<executeTime[l])
                {
                   executeFlag[l]=0; 
                }
                
                executeFlag[k]=1;
                
                
                if( k!=l &&  periodTemp[l]<periodTemp[k] && periodTemp[k]!=0 && periodTemp[l]!=0 && executeCounter[l]==executeTime[l] 
                              && executeCounter[k]<executeTime[k])
                        {
                            executeFlag[k]=0;
                        }
                
                }// if end
                     
           }//k for end
          }//l for end
          
          for(int idlectr=0;idlectr<taskCount;idlectr++)
              {
                  if(executeFlag[idlectr]==0)
                  //System.out.println("Flag of task "+ (idlectr+1) + "deactivated ");
                  
                  if(executeFlag[idlectr]==1)
                  {
                      nullFlag++;
                  }
              }
              
               if(nullFlag==taskCount)
                  {
                      System.out.print("Current time(second): " + timeStamp + " " );
                      System.out.println("processor is idle");
                      timeStamp++;
                      nullFlag=0;
                  }
                  else
                  {
                      nullFlag=0;
                  }
          
          for(int x=0;x<taskCount;x++)
          {
              
              if((taskIndex[x])< (timeStamp/period[x]))
                {
                   System.out.println("deadline of task "+(x+1)+ "exceeded,given tasks are not possible to schedule ");
                   System.exit(1);
                }  
              
              
              if(executeFlag[x]==0 && executeCounter[x]<executeTime[x])
              {
                 System.out.print("Current Time: "+ timeStamp +"task "+ (x+1) +"-" + taskIndex[x]+ "isleniyor");
                 
                 
                 executeCounter[x]++;
                 timeStamp++;
                 
                 //System.out.println(" execut edildigi miktar: "+ executeCounter[x]);
              
              
                 if(executeCounter[x]==executeTime[x])
                 {
                    taskIndex[x]++;
                    periodTemp[x] +=  period[x];
                    //System.out.println("new deadline of task "+ (x+1) + " is " + periodTemp[x]);
                    executeFlag[x]=1;
                    //System.out.println("Flag of task "+ (x+1) + "activated ");
                    
                 }//periodTemp if end
                 break;
               }//executeFlag if end
          }//x for end
        }//timeStamp for end
         
     }//choice else if end
     
     else
     {
         System.out.println("Dogru giris yapilmadi, sizden istenen degerleri dogru girerek tekrar deneyiniz");
         System.exit(1);
     }//choice else end
 }
  
}
