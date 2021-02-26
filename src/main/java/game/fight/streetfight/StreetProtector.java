package game.fight.streetfight;

import game.characters.Sorino;
import game.fight.streetfight.protectors.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public interface StreetProtector extends Serializable {
    String getName();
    String getStreetName();

    String getSarcasticRemark();
    String getWinningRemark();
    String getLosingRemark();

    Sorino getGuardianSorino();

    interface GetProtector {
        StreetProtector getProtector();
    }

    @SuppressWarnings("unused")
    enum Protectors implements GetProtector{
        LIGHTRINO {
            @Override
            public StreetProtector getProtector() {
                return new Lightrino();
            }
        },
        PEWTINI {
            @Override
            public StreetProtector getProtector() {
                return new Pewtini();
            }
        },
        TECHOTIA {
            @Override
            public StreetProtector getProtector() {
                return new Techotia();
            }
        },
        XERXES {
            @Override
            public StreetProtector getProtector() {
                return new Xerxes();
            }
        },
        SUMATO {
            @Override
            public StreetProtector getProtector() {
                return new Sumato();
            }
        },
        SHIZEN {
            @Override
            public StreetProtector getProtector() {
                return new Shizen();
            }
        },
        SOSHU {
            @Override
            public StreetProtector getProtector() {
                return new Soshu();
            }
        },
        IKARI {
            @Override
            public StreetProtector getProtector() {
                return new Ikari();
            }
        },
        CHIKARA {
            @Override
            public StreetProtector getProtector() {
                return new Chikara();
            }
        },
        TAMOTSU {
            @Override
            public StreetProtector getProtector() {
                return new Tamotsu();
            }
        };

        public static List<StreetProtector> getAllProtectors(){
            ArrayList<StreetProtector> protectors =
                    new ArrayList<>();

            for(Protectors protector : EnumSet.allOf(Protectors.class))
                protectors.add(protector.getProtector());
            return protectors;
        }

        public static Optional<StreetProtector> containsProtector(String command){
            for(Protectors protector : EnumSet.allOf(Protectors.class))
                if(command.toUpperCase().contains(protector.getProtector().getName().toUpperCase()))
                    return Optional.of(protector.getProtector());
            return Optional.empty();
        }
    }
}
