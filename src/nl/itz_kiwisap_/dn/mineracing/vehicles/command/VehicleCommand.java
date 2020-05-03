package nl.itz_kiwisap_.dn.mineracing.vehicles.command;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.itz_kiwisap_.dn.mineracing.vehicles.Main;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.Car;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.Vehicle;
import nl.itz_kiwisap_.dn.mineracing.vehicles.vehicles.objects.types.BaseType;

public class VehicleCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player p = (Player) sender;
		
		if(args.length >= 1) {
			if(args[0].equalsIgnoreCase("spawn")) {
				BaseType baseType = (new ArrayList<>(Main.getManager().getBaseTypes().values())).get(0);
				Car car = new Car(baseType, p);
				Main.getManager().getVehicles().add(car);
				
				Vehicle vehicle = Main.getManager().getVehicles().get(0);
				vehicle.spawn(p.getLocation(), p);
				p.sendMessage("vehicle spawned");
			}
		}
		
		return false;
	}

}
