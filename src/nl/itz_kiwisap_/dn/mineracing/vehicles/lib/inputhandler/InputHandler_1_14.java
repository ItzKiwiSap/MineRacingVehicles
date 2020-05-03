package nl.itz_kiwisap_.dn.mineracing.vehicles.lib.inputhandler;

import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;

import net.minecraft.server.v1_14_R1.PacketPlayInSteerVehicle;
import nl.itz_kiwisap_.dn.mineracing.vehicles.Main;
import nl.itz_kiwisap_.dn.mineracing.vehicles.Methods;

public class InputHandler_1_14 implements InputHandler {

	public void handleInput() {
		Main.getInstance().getProtocol().addPacketListener((PacketListener) new PacketAdapter((Plugin) Main.getInstance(), ListenerPriority.LOWEST, new PacketType[] { PacketType.Play.Client.STEER_VEHICLE}) {
			public void onPacketReceiving(PacketEvent packetEvent) {
				if(packetEvent.getPacketType().equals(PacketType.Play.Client.STEER_VEHICLE)) {
					boolean bool4, bool3, bool2, bool1;
					PacketPlayInSteerVehicle packetPlayInSteerVehicle = (PacketPlayInSteerVehicle) packetEvent.getPacket().getHandle();
					float f1 = packetPlayInSteerVehicle.c();
					float f2 = packetPlayInSteerVehicle.b();
					
					if(f1 > 0.0F) {
						bool1 = true;
						bool3 = false;
					} else if(f1 < 0.0F) {
						bool1 = false;
						bool3 = true;
					} else {
						bool1 = false;
						bool3 = false;
					}
					
					if(f2 > 0.0F) {
						bool2 = true;
						bool4 = false;
					} else if(f2 < 0.0F) {
						bool2 = false;
						bool4 = true;
					} else {
						bool2 = false;
						bool4 = false;
					}
					Methods.handleInput(bool1, bool2, bool3, bool4, packetPlayInSteerVehicle.d(), packetEvent.getPlayer());
				}
			}
		});
	}
}
