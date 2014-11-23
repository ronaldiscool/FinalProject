import java.util.Vector;


public class Doctor extends Villager {
	
	public Doctor(String name, ServerThread st, ServerReader reader) {
		super(name, st, reader);
	}
	
	// the player this person picks will be saved for one night
	public void power(Player p) {
		try {
			GameServer.alldocSem.acquire();
			if(p==null){nobodyVote++;}
			else
				p.tally++;
			if(GameServer.alldocSem.availablePermits()!=0)
			{
			GameServer.lock.lock();
			GameServer.alldocvotes.await();
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
				if(maxTally<nobodyVote)
					mostVoted=null;
				if(mostVoted!=null)
				{
					mostVoted.setStayAlive(true);
				}
				GameServer.lock.lock();
				GameServer.alldocvotes.signalAll();
				GameServer.lock.unlock();
			}                                
			GameServer.lock.lock();
			GameServer.alldocSem.release();
			GameServer.doctorDone=true;
				GameServer.doctorDone2.signalAll();
			GameServer.lock.unlock();
			nobodyVote=0;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getRole() {
		return "Doctor";
	}
}
