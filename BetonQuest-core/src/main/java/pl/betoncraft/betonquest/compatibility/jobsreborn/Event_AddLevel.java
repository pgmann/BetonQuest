package pl.betoncraft.betonquest.compatibility.jobsreborn;

import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.Job;
import com.gamingmesh.jobs.container.JobProgression;
import org.bukkit.entity.Player;
import pl.betoncraft.betonquest.Instruction;
import pl.betoncraft.betonquest.InstructionParseException;
import pl.betoncraft.betonquest.api.QuestEvent;
import pl.betoncraft.betonquest.utils.PlayerConverter;

import java.util.List;

public class Event_AddLevel extends QuestEvent
{
	private String sJobName;
	private Integer nAddLevel;
	
    public Event_AddLevel(Instruction instruction) throws InstructionParseException
	{
		super(instruction);
		if (instruction.size() < 3) 
		{
			throw new InstructionParseException("Not enough arguments");
		}
		for (Job job : Jobs.getJobs()) 
		{
			if (job.getName().equalsIgnoreCase(instruction.getPart(1)))
			{
				sJobName = job.getName();
				try {
					this.nAddLevel = Integer.parseInt(instruction.getPart(2));
				} catch (Exception err)
				{
					throw new InstructionParseException("NUJobs_AddLevel: Unable to parse the level amount" );
				}
				return;
			}
		}
		throw new InstructionParseException("Jobs Reborn job " + instruction.getPart(1) + " does not exist" );
    }

    @Override
    public void run(String playerID) 
    {
		Player oPlayer =  PlayerConverter.getPlayer(playerID);
		
		List<JobProgression> oJobs = Jobs.getPlayerManager().getJobsPlayer(oPlayer).getJobProgression();
		for (JobProgression oJob : oJobs) 
		{
			if (oJob.getJob().getName().equalsIgnoreCase(sJobName))
			{
				//User has the job, return true
				oJob.setLevel(this.nAddLevel + oJob.getLevel());
				if (oJob.getLevel() > oJob.getJob().getMaxLevel())
					oJob.getJob().getMaxLevel();
			}
		}
    }
}