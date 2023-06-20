
import java.util.*;


public class SchedulerMain{

	List<Chromosome> firstlist;
	List<Chromosome> newlist;
	double firstlistfitness;
	double newlistfitness;
	int populationsize=1000;
	int maxgenerations=100;
	
	public static Chromosome finalson;
	
	public SchedulerMain() {
		new inputdata().takeinput();

		Utility.printInputData();
		
		//generating slots
		new TimeTable();

//		Utility.printSlots();
		
		//initialising first generation of chromosomes and puting in first arraylist
		initialisePopulation();
		
		//generating newer generation of chromosomes using crossovers and mutation
		createNewGenerations();
	}

	public void createNewGenerations(){
		
		Chromosome father=null;
		Chromosome mother=null;
		Chromosome son=null;
		
		int nogenerations=0;
		//looping max no of generations times or until suitable chromosome found
		while(nogenerations<maxgenerations){

			
			newlist=new ArrayList<Chromosome>();
			newlistfitness=0;
			int i=0;
			
			//first 1/10 chromosomes added as it is
			for(i=0;i<populationsize/10;i++){	
				newlist.add(firstlist.get(i).deepClone());		
				newlistfitness+=firstlist.get(i).getFitness();
			}
			
			//adding other members after performing crossover and mutation
			while(i<populationsize){
				
				father=selectParentRoulette();
				mother=selectParentRoulette();
		
				if(new Random().nextDouble()<inputdata.crossoverrate){
					son=crossover(father,mother);	
				}else
					son=father;
				
				customMutation(son);
				
				
				if(son.fitness==1){
					System.out.println("Selected Chromosome is:-");
					son.printChromosome();
					break;
				}
				
				newlist.add(son);
				newlistfitness+=son.getFitness();
				i++;
				
			}
			
			//if chromosome with fitness 1 found
			if(i<populationsize){
				
				System.out.println("****************************************************************************************");
				System.out.println("\n\nSuitable Timetable has been generated in the "+i+"th Chromosome of "+(nogenerations+2)+" generation with fitness 1.");
				System.out.println("\nGenerated Timetable is:");
				son.printTimeTable();
				finalson=son;
				break;
				
			}
			
			//if chromosome with required fitness not found in this generation
			firstlist=newlist;
			Collections.sort(newlist);Collections.sort(firstlist);
			System.out.println("**************************     Generation"+(nogenerations+2)+"     ********************************************\n");
			printGeneration(newlist);
			nogenerations++;

		}
	}
	
	//selecting using Roulette Wheel Selection
	public Chromosome selectParentRoulette(){
			
		firstlistfitness/=10;
		double randomdouble=new Random().nextDouble()*firstlistfitness;
		double currentsum=0;
		int i=0;

		while(currentsum<=randomdouble){
			currentsum+=firstlist.get(i++).getFitness();
		}
		return firstlist.get(--i).deepClone();

	}
		
		
	//custom mutation
	public void customMutation(Chromosome c){
				
		double newfitness=0,oldfitness=c.getFitness();
		int geneno=new Random().nextInt(inputdata.nostudentgroup);
				
		int i=0;
		while(newfitness<oldfitness){

			c.gene[geneno]=new Gene(geneno);
			newfitness=c.getFitness();
			i++;
			if(i>=500000) break;
		}
				
	}	
		
	
	//Two point crossover
	public Chromosome crossover(Chromosome father,Chromosome mother){
			
		int randomint=new Random().nextInt(inputdata.nostudentgroup);
		Gene temp=father.gene[randomint].deepClone();
		father.gene[randomint]=mother.gene[randomint].deepClone();
		mother.gene[randomint]=temp;
		if(father.getFitness()>mother.getFitness())return father;
		else return mother;
		
	}
	
	//initialising first generation of population
	public void initialisePopulation(){
		firstlist=new ArrayList<Chromosome>();
		firstlistfitness=0;
		
		for(int i=0;i<populationsize;i++){
		
			Chromosome c;
			firstlist.add(c=new Chromosome());
			firstlistfitness+=c.fitness;
			
		}
//		Collections.sort(firstlist);
		System.out.println("----------Initial Generation-----------\n");
		printGeneration(firstlist);
	}
	
	
	public void printGeneration(List<Chromosome> list){
		
		System.out.println("Fetching details from this generation...\n");	
		
		//to print only initial 4 chromosomes of sorted list
		for(int i=0;i<4;i++){
			System.out.println("Chromosome no."+i+": "+list.get(i).getFitness());
			list.get(i).printChromosome();
			System.out.println("");
		}
		
		System.out.println("Chromosome no. "+(populationsize/10+1)+" :"+list.get(populationsize/10+1).getFitness()+"\n");
		System.out.println("Chromosome no. "+(populationsize/5+1)+" :"+list.get(populationsize/5+1).getFitness()+"\n");
		System.out.println("Most fit chromosome from this generation has fitness = "+list.get(0).getFitness()+"\n");
		
	}
	public static void main(String[] args) {
		new SchedulerMain();
	}
}