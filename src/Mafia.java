import java.util.Vector;


class Mafia extends Player {
	private int nobodyVote;
	public Mafia(String name, ServerThread st, ServerReader reader) {
		super(name, st, reader);
	}
	public void power(Player p) {
		try {
			if(GameServer.doctors.size()>0&&!GameServer.doctorDone)
			{
				GameServer.lock.lock();
			GameServer.doctorDone2.await();
			GameServer.lock.unlock();
			}
			if(GameServer.cops.size()>0&&!GameServer.copDone)
			{
				GameServer.lock.lock();
			GameServer.copDone2.await();
			GameServer.lock.unlock();
			}

			GameServer.allmafSem.acquire();
			if(p==null){nobodyVote++;}
			else
				p.tally++;
			boolean acq = false, mafneed = false, docneed=false, copneed=false;
			if(GameServer.allmafSem.availablePermits()!=0)
			{
			GameServer.lock.lock();
			GameServer.allmafvotes.await();
			GameServer.lock.unlock();
			System.out.println("DONE WAITING)");
			}
			else
			{
				acq=true;
				//Find Majority
				int maxTally = 0;
				Player mostVoted = null;
				boolean tie = false;
				for(Player p0:GameServer.players)
				{		
					if(maxTally == p0.tally && maxTally!=0)
					{
						mostVoted=null;
						p0.tally=0;
						tie=true;
					}
					else if(maxTally<p0.tally)
					{	maxTally=p0.tally;
					mostVoted=p0;
					tie=false;}
					p0.tally=0;
				}
				if(maxTally<nobodyVote)
				{
					mostVoted=null;
					acq=false;
				}
				if(mostVoted!=null)
				{
					//Kill Player if doctor has not done anything
					String targetrole = mostVoted.getRole();
					if(!mostVoted.getStayAlive())
					{
					switch(targetrole)
					{
					case "Villager":
						GameServer.villagers.remove(mostVoted);
						break;
					case "Mafia":
						GameServer.mafia.remove(mostVoted);
						break;
					case "Cop":
						GameServer.cops.remove(mostVoted);
						break;
					case "Doctor":
						GameServer.doctors.remove(mostVoted);
						break;
					}
					Vector killmv = new Vector<Player>();
					killmv.add(mostVoted);
					GameServer.sendMessage("~~DIE~~", killmv);
					
					GameServer.players.remove(mostVoted);
					}
					}
				GameServer.lock.lock();
				GameServer.allmafvotes.signalAll();
				GameServer.lock.unlock();
				if(mostVoted!=null&&!mostVoted.getStayAlive())
				{
					GameServer.sendMessage("~~KILLED~~", null);
					GameServer.sendMessage(mostVoted.getName(), null);
					GameServer.sendMessage(mostVoted.getRole(),null);
					
				}
				else if (mostVoted==null)
				{
					GameServer.sendMessage("~~KILLED~~", null);
					GameServer.sendMessage("~~~~~", null);
				}
				else if(mostVoted!= null && mostVoted.getStayAlive())
				{
					mostVoted.setStayAlive(false);
					GameServer.sendMessage("~~SAVED~~",null);
					GameServer.sendMessage(mostVoted.getName(), null);
					mostVoted.setStayAlive(false);
					GameServer.lock.lock();
					GameServer.allmafSem.release();
					GameServer.mafreleased.signalAll();
					GameServer.lock.unlock();
					nobodyVote=0;
					GameServer.doctorDone=false;
					return;
				}
				if(GameServer.players.size()<=(2*GameServer.mafia.size()))
				{
					GameServer.sendMessage("~~GAME OVER~~", null);
					GameServer.sendMessage("MAFIA",null);
					return;
				}
			if(GameServer.mafia.size()==0)
			{
				GameServer.sendMessage("~~GAME OVER~~", null);
				GameServer.sendMessage("VILLAGERS",null);
				return;
			}
			if(null!=mostVoted&&!mostVoted.getStayAlive())
			{
				String targetrole = mostVoted.getRole();
				System.out.println("ACQUIRING");
				switch(targetrole)
				{
				case "Mafia":
					mafneed=true;
					break;
				case "Cop":
					copneed=true;
					break;
				case "Doctor":
					docneed=true;
					break;
				}
				System.out.println("ACQUIRED");
			}
			}                                
			GameServer.lock.lock();
			GameServer.allmafSem.release();
			GameServer.mafreleased.signalAll();
			GameServer.lock.unlock();
			if(acq){
			System.out.println("General Permits"+GameServer.allvotesSem.availablePermits());
			GameServer.allvotesSem.acquire();
			System.out.println("General Permits"+GameServer.allvotesSem.availablePermits());}
			if(mafneed)
				GameServer.allmafSem.acquire();
			if(docneed)
				GameServer.alldocSem.acquire();

			nobodyVote=0;
			GameServer.doctorDone=false;
			GameServer.copDone=false;
			for(Player p0 : GameServer.players)
				p0.setStayAlive(false);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getRole() {
		return "Mafia";
	}
	
}