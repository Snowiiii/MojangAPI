import de.snowii.GameProfile;
import de.snowii.MojangAPI;

import java.util.UUID;

public class MojangAPITest {

    public static void main(String[] args) {
        // 7bd9c2cb-079f-4f5b-925d-4bffdcf24aa8
        long mils = System.currentTimeMillis();
        GameProfile profile = MojangAPI.getGameProfile(UUID.fromString("7bd9c2cb-079f-4f5b-925d-4bffdcf24aa8"));
        System.out.println(profile.getName());

        System.out.println(System.currentTimeMillis() - mils);
        long mils_1 = System.currentTimeMillis();
        GameProfile profile_1 = MojangAPI.getGameProfile("Hax2Snowii");
        System.out.println(profile_1.getUUID());
        System.out.println(System.currentTimeMillis() - mils_1);

    }
}
