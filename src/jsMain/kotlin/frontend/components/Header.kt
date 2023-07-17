package frontend.components

import common.RootPaths
import csstype.Display
import csstype.NamedColor
import mui.material.*
import react.FC
import react.Props
import mui.system.sx

val Header = FC<Props> {
    AppBar {
        position = AppBarPosition.static
//        sx {
//            gridArea = Area.Header
//        }

        Toolbar {
            sx {
                color = NamedColor.white
            }
            Button {
                href = RootPaths().toString()
                +"Test task"
                sx {
                    display = Display.block
                    color = NamedColor.aqua
                }
            }
            Button {
                    href = RootPaths().Weapons.toString()
                    +"Weapons "
                sx {
                    display = Display.block
                    color = NamedColor.white
                }
            }
            Button {
                href = RootPaths().Heroes.toString()
                +"Heroes"
                sx {
                    display = Display.block
                    color = NamedColor.white
                }
            }
            Button {
                href = RootPaths().Commit.toString()
                +"Changes"
                sx {
                    display = Display.block
                    color = NamedColor.white
                }
            }
        }

    }

}
