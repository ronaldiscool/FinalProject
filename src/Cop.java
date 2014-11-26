import java.util.Vector;


class Cop extends Villager{

	public Cop(String name, ServerThread st, ServerReader reader) {
		super(name, st, reader);
	}
	
	public void power(Player p) {
		try {
			GameServer.allcopSem.acquire();
			if(p==null){nobodyVote++;}
			else
				p.tally++;
			if(GameServer.allcopSem.availablePermits()!=0)
			{
			GameServer.lock.lock();
			GameServer.allcopvotes.await();
			GameServer.lock.unlock();
			}
			else
			{
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
				if(maxTally<nobodyVote||tie)
				{
					mostVoted=null;
				}

				if(mostVoted!=null)
				{
					boolean isMafia = false;
					for(Player p1: GameServer.mafia)
					{
						if(mostVoted == p1)
						{
							isMafia = true;
							break;
						}
					}
					GameServer.sendMessage("~~REPORT~~",GameServer.cops);
					if(isMafia)
					{
						GameServer.sendMessage(mostVoted.getName() + " is part of the Mafia.", GameServer.cops);
					}
					else 
					{
						GameServer.sendMessage(mostVoted.getName() + " is not part of the Mafia.", GameServer.cops);
					}

				}
				GameServer.lock.lock();
				GameServer.allcopvotes.signalAll();
				GameServer.copDone=true;
				GameServer.copDone2.signalAll();
				GameServer.lock.unlock();
			}                                
			GameServer.lock.lock();
			GameServer.allcopSem.release();
			GameServer.released.signalAll();
			GameServer.lock.unlock();
			nobodyVote=0;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public String getRole() {
		return "Cop";
	}
}
