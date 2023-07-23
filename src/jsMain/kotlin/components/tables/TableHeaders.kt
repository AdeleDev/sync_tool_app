package components.tables

import mui.material.TableCell
import mui.material.TableHead
import mui.material.TableRow
import react.FC
import react.Props

val HeroColumnHeader = FC<Props> {
    TableHead {
        TableRow {
            TableCell {
                +"Hero Name"
            }
            TableCell {
                +"Main image"
            }
            TableCell {
                +"Icon"
            }
            TableCell {
                +"Actions"
            }
        }
    }
}

val WeaponColumnHeader = FC<Props> {
    TableHead {
        TableRow {
            TableCell {
                +"Hero Name"
            }
            TableCell {
                +"Main image"
            }
            TableCell {
                +"Entire Icon"
            }
            TableCell {
                +"Broken icon"
            }
            TableCell {
                +"Actions"
            }
        }
    }
}
