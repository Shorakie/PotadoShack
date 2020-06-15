package ir.aminer.potadoshack.server.listeneres;

import ir.aminer.potadoshack.core.error.Error;
import ir.aminer.potadoshack.core.event.Listener;
import ir.aminer.potadoshack.core.event.ListenerMethod;
import ir.aminer.potadoshack.core.event.events.UpdatePasswordEvent;
import ir.aminer.potadoshack.core.event.events.UpdateProfileEvent;
import ir.aminer.potadoshack.core.network.packets.PrimitivePacket;
import ir.aminer.potadoshack.core.network.packets.ResponsePacket;
import ir.aminer.potadoshack.core.user.BaseUser;
import ir.aminer.potadoshack.core.utils.Common;
import ir.aminer.potadoshack.server.PotadoShackServer;
import ir.aminer.potadoshack.server.User;

import java.io.IOException;

public class UpdatesListener implements Listener {

    @ListenerMethod
    public void onUpdateProfile(UpdateProfileEvent event) throws IOException {
        User user = event.getUser();
        if (user == null)
            return;

        BaseUser newUser = event.getData().getUser();
        user.setFirstName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());
        user.setEmail(newUser.getEmail());
        user.setPhoneNumber(newUser.getPhoneNumber());
        user.setProfilePicture(newUser.getProfilePicture());

        user.save();
        event.getSender().sendResponse(new PrimitivePacket<>("OK"), ResponsePacket.Status.OK);
    }

    @ListenerMethod
    public void onUpdatePassword(UpdatePasswordEvent event) throws IOException {
        User user = event.getUser();
        if (user == null)
            return;

        String password = Common.hmacSha256(PotadoShackServer.SECRET_KEY, event.getData().getCurrentPassword());
        if (!user.getPassword().equals(password)){
            event.getSender().sendError(Error.WRONG_PASSWORD);
            return;
        }

        user.setPassword(Common.hmacSha256(PotadoShackServer.SECRET_KEY, event.getData().getRawPassword()));
        user.save();
        event.getSender().sendResponse(new PrimitivePacket<>("OK"), ResponsePacket.Status.OK);
    }
}
