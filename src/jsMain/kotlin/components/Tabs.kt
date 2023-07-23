package components

import csstype.Color
import csstype.pct
import mui.material.Box
import mui.material.Tab
import mui.material.Tabs
import mui.system.sx
import react.*

external interface TabProps : Props {
    var localChangeSetter: StateSetter<Boolean>
    var userName: String
}

val TabsShowcase = FC<TabProps> { props ->
    var activeTab by useState("one")

    Box {
        sx {
            width = 100.pct
        }

        Tabs {
            value = activeTab
            onChange = { _, newValue -> activeTab = newValue }

            Tab {
                sx {
                    color = Color("#12445e")
                }
                value = "one"
                label = ReactNode("Existing items")
                onClick = {
                    props.localChangeSetter.invoke(false)
                }
            }
            if (props.userName === "drawer" || props.userName == "drawer2") {
                Tab {
                    sx {
                        color = Color("#12445e")
                    }
                    value = "two"
                    label = ReactNode("Local changes")
                    onClick = {
                        props.localChangeSetter.invoke(true)
                    }
                }
            }
        }
    }
}