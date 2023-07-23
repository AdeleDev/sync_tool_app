import components.Footer
import components.Header
import components.TabsShowcase
import components.User
import components.tables.EditItem
import components.tables.EditProps
import components.tables.existingitems.ItemList
import components.tables.localchanges.DraftItemList
import kotlinx.js.jso
import react.*
import react.router.Route
import react.router.Routes
import react.router.dom.HashRouter


const val BASE_URL = "http://localhost:8080/v1/element/"


external interface MainProps : Props {
    // var setEditElement: StateSetter<String>
    var userId: String
    var setEditorUserId: StateSetter<String>
}

fun getStubUserId(user: String): Int {
    when (user) {
        "drawer" -> return 1
        "viewer" -> return 2
        "designer" -> return 3
        "drawer2" -> return 4
        else -> {
            print("Unknown user")
        }
    }
    return 0
}

val App = FC<MainProps> {
    val (editorId, setEditor) = useState("drawer")

    val heroEditProps: EditProps = jso {
        type = "hero"
        userId = editorId
    }

    val weaponEditProps: EditProps = jso {
        type = "weapon"
        userId = editorId
    }

    val mainProps: MainProps = jso {
        userId = editorId
        setEditorUserId = setEditor
    }

    Header()

    HashRouter {
        Routes {
            Route {
                path = "/"
                element = createElement(MainBlock, mainProps)
            }
            Route {
                path = "hero/*"
                element = createElement(EditItem, heroEditProps)
            }
            Route {
                path = "weapon/*"
                element = createElement(EditItem, weaponEditProps)
            }
        }
    }

    Footer()
}


val MainBlock = FC<MainProps> { props ->

    val (elementConter, addNewElement) = useState(0)
    val (heroNames, setHeroesNames) = useState(arrayOf("No data"))
    val (rowsHero, setRowsHeroes) = useState(
        arrayOf(
            js("{'name': 'Example row..no data', 'img':'', 'icon':''}"),
        )
    )

    val (weaponNames, setWeaponNames) = useState(arrayOf("No data"))
    val (rowsWeapon, setRowsWeapon) = useState(
        arrayOf(
            js("{'name': 'Example row..no data', 'img':'', 'icon':'', 'icon2':''}"),
        )
    )

    val (rowsHeroDraft, setRowsHeroesDraft) = useState(
        arrayOf(
            js("{'name': 'No draft..', 'img':'', 'icon':''}"),
        )
    )


    val (rowsWeaponDraft, setRowsWeaponDraft) = useState(
        arrayOf(
            js("{'name': 'No draft...', 'img':'', 'icon':'', 'icon2':''}"),
        )
    )


    val (localChanges, openLocalChanges) = useState(false)


    fetchMainData("hero", setHeroesNames, elementConter)
    fetchMainData("weapon", setWeaponNames, elementConter)
    fetchImageData("hero", heroNames, setRowsHeroes)
    fetchImageData("weapon", weaponNames, setRowsWeapon)

    fetchAllDraftData("hero", heroNames, props.userId, setRowsHeroesDraft)
    fetchAllDraftData("weapon", weaponNames, props.userId, setRowsWeaponDraft)


    User {
        userName = props.userId
        userSetter = props.setEditorUserId
        refresh = addNewElement
    }
    TabsShowcase {
        localChangeSetter = openLocalChanges
        userName = props.userId
    }
    if (localChanges) {
        DraftItemList {
            heroItemList = rowsHeroDraft
            weaponItemList = rowsWeaponDraft
            userName = props.userId
            refreshImage = addNewElement
        }
    } else {
        ItemList {
            heroItemList = rowsHero
            heroListSetter = setRowsHeroes
            weaponItemList = rowsWeapon
            weaponListSetter = setRowsWeapon
            userName = props.userId
            elementCounter = addNewElement
        }
    }
}