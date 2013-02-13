import bv.brainP5.*;

/**

MOUSE MOTION PREDICTION
by Bogdan Vera

In this example two NNs try to predict the velocity of the mouse cursor by half a
second (50 frames at 100fps). Hold a mouse button down and start moving the cursor 
in repeating patterns. After a while the red circle, which represents the predicted position
will (hopefully) start to follow the mouse cursor. The circle is the current mouse position
predicted from 50 samples, from 50 samples ago (i.e. 100 samples ago), so it has a bit of latency
in its response, but this is designed to show how well the prediction matches the mouse movement,
otherwise if it was predicting from the previous 50 samples it would show a position ahead of your cursor.
Also note that the red circle will not necessarily coincide with the mouse position, as it uses ONLY
velocity to train and predict, not position. What is predicted is the motion and not the position.

This example works well if you make repeating gestures very quickly. For slow gestures, the entire
gesture is not represented within 50 samples so the NN fails to train for change in direction. To
fix this you would have to increase the number of samples used, but then it would be harder to train.

Also note that the network is trained in real time, so the more you play with it the better it 
will predict. It will be completely useless at the start. You have to repeat gestures a lot 
for it to learn them. 

I will try to improve this example. 

*/
FeedForwardNN n1;
  FeedForwardNN n2;
  
  //stores the motion points
  ArrayList<Location> motion;
  /**
   * stores the predict motion points so we can draw 
   * the predicted points late and see how well it works.
   */
  ArrayList<PVector> predictedMotion;
  public void setup() 
  {
    n1 = new FeedForwardNN(this, 0.8f, -1f, 1f);
    n2 = new FeedForwardNN(this, 0.8f, -1f, 1f);
    n1.populate(50,30, 1);
    n2.populate(50,30, 1);
    size(800,800);
    frameRate(100);
    
    motion = new ArrayList<Location>();
    predictedMotion= new ArrayList<PVector>();
  }

  //where to draw the follower
  float vx = 0.0f;
  float vy = 0.0f;
  
  public void draw() 
  {
    background(0);
    
    if(mousePressed)
    {
      Location prevLoc;
      if(motion.size()>0)
      prevLoc = motion.get(motion.size()-1);
      else
      prevLoc = new Location(0, 0);

      motion.add(new Location(mouseX,mouseY,prevLoc));
      //learn to predict 100 samples in advance using 100 previous input samples
      if(motion.size()>200)
      {
        Sample samp1 = new Sample(this);
      for(int i = 0; i<50;i++)
      {
        n1.prepare();
        
        //we need to put the vector in the 0.0 to 1.0 range so we can scale it back...
        float vecx = (motion.get(motion.size()-1 - 50-i).vel.x/800.0f + 0.5f);
          println("Putting vecx: " + vecx);
          samp1.addInput(vecx);
        
        
      }
      samp1.addOutput(((float)motion.get(motion.size()-1).vel.x)/800.0f + 0.5f);
      n1.trainLive(samp1);
      Sample samp2 = new Sample(this);
      for(int i = 0; i<50;i++)
      {
        n2.prepare();
        
        
        
        float vecy = (motion.get(motion.size()-1 - 50-i).vel.y/800.0f+ 0.5f);
          samp2.addInput(vecy);
         
      }
      samp2.addOutput(((float)motion.get(motion.size()-1).vel.y)/800.0f+ 0.5f);
      n2.trainLive(samp2);
      
      //now predict from the last 100 samples
      
      if(motion.size()>50)
      {
      //train the network with just the two previous x,y to put out new x y
        Sample samp3 = new Sample(this);
        for(int i = 0; i<100;i++)
        {
        n1.prepare();
        
        
        float vecx = (motion.get(motion.size()-1-i).vel.x/800.0f+ 0.5f);
          samp3.addInput(vecx);

        }
        n1.prepare();
        n1.process(samp3);
        Sample samp4 = new Sample(this);
        for(int i = 0; i<50;i++)
        {
        n2.prepare();
     
        float vecy = (motion.get(motion.size()-1-i).vel.y/800.0f+ 0.5f);
          samp4.addInput(vecy);

        }
        n2.prepare();
        n2.process(samp4);
      }
      float nx,ny;
      nx = n1.outputNeuron(0).getOutput() - 0.5f;
      ny = n2.outputNeuron(0).getOutput() - 0.5f;
      println("Predicted a vector with " + nx + " " + ny);
      
      predictedMotion.add(new PVector(nx*800.0f,ny*800.0f));
  
      if(predictedMotion.size()>100)
      {
        
        
        
       //how well did we predict?
        println("**********PREDICTION QUALITY**********");
        
        //we are predicting 50 samples in advance, so get a sample from 50 frames ago
        PVector oldVect = predictedMotion.get(predictedMotion.size()-1);
        PVector nowVect = new PVector(motion.get(motion.size()-1).vel.x, motion.get(motion.size()-1).vel.y);
        
       
        float error = dist(nowVect.x, nowVect.y, oldVect.x, oldVect.y);
        println("ERROR: " + error);
        
        
      PVector predictionLate = new PVector(predictedMotion.get(predictedMotion.size()-1-50).x, 
                        predictedMotion.get(predictedMotion.size()-1-50).y);
      
      vx+=predictionLate.x;
      vy+=predictionLate.y;
      //println(vx + " " + vy);
      
      fill(255,0,0);
      ellipse(vx,vy,40,40);
      }
      
      }
    
      if(motion.size()>0)
      {
        motion.get(motion.size()-1).printDetails();
      }
    }

  }
  
  @Override
  public void mousePressed()
  {
    vx = mouseX;
    vy = mouseY;
  }
  
  @Override
  public void mouseReleased()
  {
    
  }

/**
 * Stores information about one position including velocity and acceleration for the 
 * moving object at this position as PVectors.
 * @author bv301
 *
 */
public class Location 
{
  PVector pos;
  PVector vel;
  PVector acc;
 
  
  /**
   * Constructor takes in x,y and previous position 
   * to determine velocity and acceleration.
   * @param prevLoc
   */
  Location(float x, float y, Location prevLoc)
  {
  
    PVector prevPos = prevLoc.pos;
    PVector prevVel = prevLoc.vel;
    PVector prevAcc = prevLoc.acc;
    
    
    pos = new PVector(x,y);
    //calculate velocity
    vel = vel.sub(pos,prevPos);
    //calculate acceleration
    acc = acc.sub(vel,prevVel);
  }
  
  Location(float x, float y)
  {

    
    PVector prevPos = new PVector(0,0);
    PVector prevVel = new PVector(0,0);
    PVector prevAcc = new PVector(0,0);
    
    
    pos = new PVector(x,y);
    //calculate velocity
    vel = vel.sub(pos,prevPos);
    //calculate acceleration
    acc = acc.sub(vel,prevVel);
  }
  
  void printDetails()
  {
    println("***Location details***");
    println("Pos: " + pos);
    println("Vel: " + vel);
    println("Acc: " + acc);
  }
}