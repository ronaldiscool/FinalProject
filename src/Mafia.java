import java.util.Vector;


class Mafia extends TheMafia {
	
	public Mafia(String name, ServerThread st, ServerReader reader) {
		super(name, st, reader);
	}
	public void power(Player p) {
		try {
			System.out.println("DDDDDDDDDDDD");
			if(GameServer.doctors.size()>0&&!GameServer.doctorDone)
			{
				System.out.println(GameServer.doctors.size()+"::A:A:A:A");
				GameServer.lock.lock();
			GameServer.doctorDone2.await();
			GameServer.lock.unlock();
			}
			System.out.println("MAFFFFFFearly"+GameServer.allmafSem.availablePermits());

			GameServer.allmafSem.acquire();
			System.out.println("MAFFFFFF"+GameServer.allmafSem.availablePermits());
			if(p==null){nobodyVote++;}
			else
				p.tally++;
			if(GameServer.allmafSem.availablePermits()!=0)
			{
			GameServer.lock.lock();
			GameServer.allmafvotes.await();
			GameServer.lock.unlock();
			System.out.println("DONE WAITING)");
			}
			else
			{
				//Find Majority
				int maxTally = 0;
				Player mostVoted = null;
				for(Player p0:GameServer.players)
				{		
					if(maxTally == p0.tally && maxTally!=0)
					{
						mostVoted=null;
						break;
					}
					if(maxTally<p0.tally)
					{	maxTally=p0.tally;
					mostVoted=p0;}
				}
				if(maxTally<nobodyVote)
					mostVoted=null;
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
					case "Mafia":
						GameServer.mafia.remove(mostVoted);
					case "Cop":
						GameServer.cops.remove(mostVoted);
					case "Doctor":
						GameServer.doctors.remove(mostVoted);
					}
					Vector killmv = new Vector<Player>();
					killmv.add(mostVoted);
					GameServer.sendMessage("~~DIE~~", killmv);
					GameServer.st.remove(GameServer.players.indexOf(mostVoted));
					GameServer.readers.remove(GameServer.players.indexOf(mostVoted));
					GameServer.names.remove(GameServer.players.indexOf(mostVoted));
					GameServer.players.remove(GameServer.players.indexOf(mostVoted));
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
				else
				{
					mostVoted.setStayAlive(false);
					GameServer.sendMessage("~~SAVED~~",null);
					GameServer.sendMessage(mostVoted.getName(), null);
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
			if(null!=mostVoted)
			{
				GameServer.lock.lock();
				GameServer.mafreleased.await();
				GameServer.lock.unlock();
				GameServer.allvotesSem.acquire();
				String targetrole = mostVoted.getRole();
				System.out.println("ACQUIRING");
				switch(targetrole)
				{
				case "Mafia":
					GameServer.allmafSem.acquire();
					break;
				//case "Cop":
					//GameServer.cops.remove(mostVoted);
				case "Doctor":
					GameServer.alldocSem.acquire();
					break;
				}
				System.out.println("ACQUIRED");
			}
			}                                
			GameServer.lock.lock();
			GameServer.allmafSem.release();
			GameServer.mafreleased.signalAll();
			GameServer.lock.unlock();
			nobodyVote=0;
			GameServer.doctorDone=false;
			System.out.println("MMMMMMMMMMMM");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getRole() {
		return "Mafia";
	}
	
}