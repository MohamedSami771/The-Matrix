package code;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;



public class NeoState extends State{
	Point neo;
	int c;
	Point telephoneBooth;
	ArrayList<Agent> agents;
	ArrayList<Point> pills; 
	HashMap<String, String> pads;
	ArrayList<Hostage> hostages;
	ArrayList<Hostage> carriedHostages;
	int damage; //cumulative or just the new damage?
	boolean tookPill = false;
	String[][] grid;
	boolean pill;
	
	public NeoState(Point neo, int c, Point telephoneBooth, ArrayList<Agent> agents, ArrayList<Point> pills, 
			HashMap<String, String> pads, ArrayList<Hostage> hostages, ArrayList<Hostage> carriedHostages, boolean tookPill, int damage, int m, int n) {
		
		super();
		this.neo = neo;
		this.c = c;
		this.telephoneBooth = telephoneBooth;
		this.agents = agents;
		this.pills = pills;
		this.pads = pads;
		this.hostages = hostages;
		this.damage = damage;
		this.pathCost = this.damage;
		this.carriedHostages = carriedHostages;
		this.tookPill = tookPill;
		this.grid = new String[m][n];
		this.heuristicOne=heuristicFunction();
		this.heuristicTwo=heuristicFunction();

	}
	
	public int pathCost() {
		int hostageAgent=0;
		for(int i=0;i<agents.size();i++) {
			if(agents.get(i).hostage)
				hostageAgent++;				
		}
		
		int pillCost=2;
		if(pill)
			pillCost=0;
		
		int carried;
		
		carried=1/(carriedHostages.size()+1);
		
		
		return (pillCost+hostageAgent+carried+damage);
		
	}
	
	public int heuristicFunction() {
		if(goalTest())
			return 0;
		
		int distance =calEuclideanDistance(this.neo.x,this.neo.y,this.telephoneBooth.x,this.telephoneBooth.y);
		if(distance==0)
			return 1;
		
		
		for (String name: pads.keySet()) {
			String key = name.toString();
		    String value = pads.get(name).toString();
		    String[] pad1 = key.split(",");
		    String[] pad2 = value.split(",");
		    
		    int x=calEuclideanDistance(this.neo.x,this.neo.y,Integer.parseInt(pad1[0]),Integer.parseInt(pad1[1]))+calEuclideanDistance(Integer.parseInt(pad2[0]),Integer.parseInt(pad2[0]),this.telephoneBooth.x,this.telephoneBooth.y);
		    
		    if(x<distance) 
		    	distance=x;
		    
		    
		    int y= calEuclideanDistance(this.neo.x,this.neo.y,Integer.parseInt(pad2[0]),Integer.parseInt(pad2[1]))+calEuclideanDistance(Integer.parseInt(pad1[0]),Integer.parseInt(pad1[0]),this.telephoneBooth.x,this.telephoneBooth.y);
		    
		    if(y<distance) 
		    	distance=y;
		    
		}
		return distance;
		}
	
	public boolean goalTest() {
		int damage = this.getDamage();
		
		if(damage >= 100) 
			return false;
		if(carriedHostages.size() != 0)
			return false;
		if(!neo.equals(telephoneBooth))
			return false;
		for(int i = 0; i < hostages.size(); i++) {
			if(hostages.get(i).x != telephoneBooth.x || hostages.get(i).y != telephoneBooth.y)
				return false;
			
		}
		for(int i = 0; i < agents.size(); i++) {
			if(agents.get(i).isHostage() && !agents.get(i).isKilled())
				return false;
			
			
		}
//		System.out.println(states.toString());
		return true;
		
	}
		
	public int calEuclideanDistance(int x,int y,int z, int f) {
		
		return (int) (Math.sqrt(Math.pow(f-y,2)+Math.pow(z-x,2)));
		
	}
		
		
//		return (Math.abs((this.telephoneBooth.y-this.neo.y)+(this.telephoneBooth.x-this.neo.x)));
		
	
	
//	public int heuristicFunctionTwo() {
//		
//	}

	@Override
	public String toString() {
		String result = "";
		result += "N" + neo.x + "," + neo.y + ";";
//		result += c + ";";
		result += "TB" + telephoneBooth.x + "," + telephoneBooth.y + ";";
		result += " A";
		for(int i = 0; i < agents.size(); i++) {
			if(i != agents.size() - 1)
				result += agents.get(i).x + "," + agents.get(i).y + "," + agents.get(i).isKilled() + "," + agents.get(i).isHostage() + ",";
			else
				result += agents.get(i).x + "," + agents.get(i).y + "," + agents.get(i).isKilled() + "," + agents.get(i).isHostage() + ";";
		}

		result += " H";

		for(int i = 0; i < hostages.size(); i++) {
			if(i != hostages.size() - 1)
				result += hostages.get(i).x + "," + hostages.get(i).y + ",";
			else
				result += hostages.get(i).x + "," + hostages.get(i).y + ";";
		}
		result += " CH";

		for(int i = 0; i < carriedHostages.size(); i++) {
			if(i != carriedHostages.size() - 1)
				result += carriedHostages.get(i).x + "," + carriedHostages.get(i).y + ",";
			else
				result += carriedHostages.get(i).x + "," + carriedHostages.get(i).y + ";" ;
		}
		result += " D";

		result +=  damage + ";" + tookPill + ";";
		
		return result;
	}
	public String populateGrid() {
		for(int i = 0; i < agents.size(); i++) {
			Agent agent = agents.get(i);
			if(!agent.isKilled())
				grid[agent.x][agent.y] = "        A        ";
		}
		for(int i = 0; i < pills.size(); i++) {
			Point pill = pills.get(i);
			grid[pill.x][pill.y] = "        P        ";
		}
		for (String name: pads.keySet()) {
			int i = 0;
		    String key = name.toString();
		    String value = pads.get(name).toString();
		    String[] pad1 = key.split(",");
		    String[] pad2 = value.split(",");
		    if(i > 9) {
		    	if ((Integer.parseInt(pad1[0]) > 9 && Integer.parseInt(pad1[0]) < 10) || (Integer.parseInt(pad1[0]) < 10 && Integer.parseInt(pad1[0]) > 9)) {
		    		grid[Integer.parseInt(pad1[0])][Integer.parseInt(pad1[1])] = "  Pad" + i + " (" + key + ")   ";
				    grid[Integer.parseInt(pad2[0])][Integer.parseInt(pad2[1])] = "  Pad" + i + " (" + value + ")   ";
		    	}
		    	
		    	else if(Integer.parseInt(pad1[0]) > 9 && Integer.parseInt(pad1[0]) > 9) {
		    		grid[Integer.parseInt(pad1[0])][Integer.parseInt(pad1[1])] = "  Pad" + i + " (" + key + ")  ";
				    grid[Integer.parseInt(pad2[0])][Integer.parseInt(pad2[1])] = "  Pad" + i + " (" + value + ")  ";
		    	}
		    		
			    else {
			    	grid[Integer.parseInt(pad1[0])][Integer.parseInt(pad1[1])] = "   Pad" + i + " (" + key + ")   ";
				    grid[Integer.parseInt(pad2[0])][Integer.parseInt(pad2[1])] = "   Pad" + i + " (" + value + ")   ";
			    }
		    }
		    else {
		    	if ((Integer.parseInt(pad1[0]) > 9 && Integer.parseInt(pad1[0]) < 10) || (Integer.parseInt(pad1[0]) < 10 && Integer.parseInt(pad1[0]) > 9)) {
		    		grid[Integer.parseInt(pad1[0])][Integer.parseInt(pad1[1])] = "   Pad" + i + " (" + key + ")   ";
				    grid[Integer.parseInt(pad2[0])][Integer.parseInt(pad2[1])] = "   Pad" + i + " (" + value + ")   ";
		    	}
		    	
		    	else if(Integer.parseInt(pad1[0]) > 9 && Integer.parseInt(pad1[0]) > 9) {
		    		grid[Integer.parseInt(pad1[0])][Integer.parseInt(pad1[1])] = "  Pad" + i + " (" + key + ")   ";
				    grid[Integer.parseInt(pad2[0])][Integer.parseInt(pad2[1])] = "  Pad" + i + " (" + value + ")   ";
		    	}
		    		
			    else {
			    	grid[Integer.parseInt(pad1[0])][Integer.parseInt(pad1[1])] = "   Pad" + i + " (" + key + ")    ";
				    grid[Integer.parseInt(pad2[0])][Integer.parseInt(pad2[1])] = "   Pad" + i + " (" + value + ")    ";
			    }
		    }
		    
		}
		for(int i = 0; i < hostages.size(); i++) {
			Hostage hostage = hostages.get(i);
			if(hostage.getDamage() < 10)
				grid[hostage.x][hostage.y] = "      H (" + hostage.getDamage() + ")      ";
			else
				grid[hostage.x][hostage.y] = "     H (" + hostage.getDamage() + ")      ";
		}
		grid[telephoneBooth.x][telephoneBooth.y] = "       TB        ";
		grid[neo.x][neo.y] = "        N        ";
		String result = "";
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if(grid[i][j] == null)
					grid[i][j] = "                 ";
				result += " " + grid[i][j] + " |";
			}
			result += "\n\n";
		}
		for (int j = 0; j < grid[0].length; j++) {
			result += "++++++++++++++++++++";
		}
		result += "\n\n";
		return result;
	}
	public String stringVisualization() {
		String result = "";
		result += "N " + neo.x + "," + neo.y + ";";
		result += "C " + c + ";";
		result += "TB " + telephoneBooth.x + "," + telephoneBooth.y + ";";
		result += " A ";
		for(int i = 0; i < agents.size(); i++) {
			if(i != agents.size() - 1)
				result += agents.get(i).x + "," + agents.get(i).y + "," + agents.get(i).isKilled() + "," + agents.get(i).isHostage() + ",";
			else
				result += agents.get(i).x + "," + agents.get(i).y + "," + agents.get(i).isKilled() + "," + agents.get(i).isHostage() + ";";
		}
		result += " P ";
		for(int i = 0; i < pills.size(); i++) {
			if(i != pills.size() - 1)
				result += pills.get(i).x + "," + pills.get(i).y + ",";
			else
				result += pills.get(i).x + "," + pills.get(i).y + ";";	
		}
		result += " PAD ";
		for (String name: pads.keySet()) {
		    String key = name.toString();
		    String value = pads.get(name).toString();
		    result += key + "," + value + ",";
		}
		result = result.substring(0, result.length() - 1) + ";";
		
		result += " H ";

		for(int i = 0; i < hostages.size(); i++) {
			if(i != hostages.size() - 1)
				result += hostages.get(i).x + "," + hostages.get(i).y + "," + hostages.get(i).getDamage() + ",";
			else
				result += hostages.get(i).x + "," + hostages.get(i).y + "," + hostages.get(i).getDamage() + ";" ;
		}
		result += " CH ";

		for(int i = 0; i < carriedHostages.size(); i++) {
			if(i != carriedHostages.size() - 1)
				result += carriedHostages.get(i).x + "," + carriedHostages.get(i).y + "," + carriedHostages.get(i).getDamage() + ",";
			else
				result += carriedHostages.get(i).x + "," + carriedHostages.get(i).y + "," + carriedHostages.get(i).getDamage() + ";" ;
		}
		result += " D ";

		return result +  damage + ";" + tookPill + ";";
	}
	public Point getNeo() {
		return neo;
	}

	public void setNeo(Point neo) {
		this.neo = neo;
	}

	public int getC() {
		return c;
	}

	public void setC(int c) {
		this.c = c;
	}

	public Point getTelephoneBooth() {
		return telephoneBooth;
	}

	public void setTelephoneBooth(Point telephoneBooth) {
		this.telephoneBooth = telephoneBooth;
	}

	public ArrayList<Agent> getAgents() {
		return agents;
	}

	public void setAgents(ArrayList<Agent> agents) {
		this.agents = agents;
	}

	public ArrayList<Point> getPills() {
		return pills;
	}

	public void setPills(ArrayList<Point> pills) {
		this.pills = pills;
	}

	public HashMap<String, String> getPads() {
		return pads;
	}

	public void setPads(HashMap<String, String> pads) {
		this.pads = pads;
	}

	public ArrayList<Hostage> getHostages() {
		return hostages;
	}

	public void setHostages(ArrayList<Hostage> hostages) {
		this.hostages = hostages;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}
	public ArrayList<Hostage> getCarriedHostages() {
		return carriedHostages;
	}
	public void setCarriedHostages(ArrayList<Hostage> carriedHostages) {
		this.carriedHostages = carriedHostages;
	}
	public boolean isTookPill() {
		return tookPill;
	}
	public void setTookPill(boolean tookPill) {
		this.tookPill = tookPill;
	}

	public boolean isPill() {
		return pill;
	}

	public void setPill(boolean pill) {
		this.pill = pill;
	}
	

}



