package components.tables

import components.ButtonStyled
import csstype.*
import emotion.react.css
import fetchEditData
import fetchEditDraftData
import mui.material.*
import mui.system.sx
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.files.File
import org.w3c.files.get
import react.FC
import react.Props
import react.StateSetter
import react.dom.events.ChangeEvent
import react.dom.events.MouseEvent
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.h3
import react.dom.html.ReactHTML.h5
import react.router.Params
import react.router.dom.Link
import react.router.useParams
import react.useState
import sendNewDraftRequest

external interface EditProps : Props {
    var type: String
    var editItem: String
    var userId: String
    var setHasDraft: StateSetter<Boolean>
    var mainImage: File?
    var icon: File?
    var entireIcon: File?
    var brokenIcon: File?
}

const val ELEMENT_NOT_EXIST_VALUE = "No element in db or failure"


val EditItem = FC<EditProps> { props ->
    val currentUser = props.userId
    val params: Params = useParams()

    val (element, setElement) = useState<dynamic>(js("{'name': 'Fetching...'}"))
    val (draft, setDraft) = useState<dynamic>(js("{'name': 'No draft...'}"))

    val editName: String? = params["*"]

    val (mainImageFile, setMainImage) = useState<File?>(null)
    val uploadMainImage = { event: ChangeEvent<HTMLElement> ->
        setMainImage((event.target as HTMLInputElement).files?.get(0))
    }
    val (iconFile, setIcon) = useState<File?>(null)
    val uploadIcon = { event: ChangeEvent<HTMLElement> ->
        setIcon((event.target as HTMLInputElement).files?.get(0))
    }
    val (entireIconFile, setEntireIcon) = useState<File?>(null)
    val uploadEntireIcon = { event: ChangeEvent<HTMLElement> ->
        setEntireIcon((event.target as HTMLInputElement).files?.get(0))
    }
    val (brokenIconFile, setBrokenIcon) = useState<File?>(null)
    val uploadBrokenIcon = { event: ChangeEvent<HTMLElement> ->
        setBrokenIcon((event.target as HTMLInputElement).files?.get(0))
    }

    val elementName = element["name"] as String
    val draftName = draft["name"] as String
    val (draftExists, setDraftExist) = useState(false)

    if (editName != null) {
        fetchEditData(props.type, editName, setElement)
        fetchEditDraftData(props.type, editName, currentUser, setDraft, draftExists, setDraftExist)
        if (elementName != ELEMENT_NOT_EXIST_VALUE) {
            div {
                h2 { +"Edit Element" }

                h2 { +elementName }
                TableContainer {
                    Table {
                        if (props.type === "hero") {
                            HeroColumnHeader()
                            TableBody {
                                TableRow {
                                    TableCell {
                                        +editName
                                    }
                                    TableCell {
                                        if (element["mainImage"] != null) {
                                            ReactHTML.img {
                                                css {
                                                    borderRadius = 8.px
                                                    width = 150.px
                                                    padding = 5.px
                                                }
                                                src = element["mainImage"]
                                                alt = "mainImage"
                                            }
                                        }
                                        Input {
                                            type = "file"
                                            onChange = uploadMainImage
                                        }
                                    }
                                    TableCell {
                                        if (element["icon"] != null) {
                                            ReactHTML.img {
                                                css {
                                                    borderRadius = 8.px
                                                    width = 150.px
                                                    padding = 5.px
                                                }
                                                src = element["icon"]
                                                alt = "icon"
                                            }
                                        }
                                        Input {
                                            type = "file"
                                            onChange = uploadIcon
                                        }
                                    }
                                    TableCell {
                                        if (draftExists) {

                                            h5 {
                                                css {
                                                    color = Color("#FFA500")
                                                }
                                                +"Warning: uncommited draft will be rewritten on new"
                                            }
                                        }
                                        SubmitLocalButtonBox {
                                            type = props.type
                                            editItem = editName
                                            userId = currentUser
                                            setHasDraft = setDraftExist
                                            mainImage = mainImageFile
                                            icon = iconFile
                                        }

                                    }
                                }
                                if (draftExists) {
                                    h3 { +"Have changes:" }

                                    TableRow {

                                        sx {
                                            backgroundColor = Color("#ADD8E6")
                                        }
                                        TableCell {
                                            +draftName
                                        }
                                        //val mainImage = draft["mainImage"] as String
                                        TableCell {
                                            if (draft["mainImage"] != null) {
                                                ReactHTML.img {
                                                    css {
                                                        borderRadius = 8.px
                                                        width = 150.px
                                                        padding = 5.px
                                                    }
                                                    src = draft["mainImage"]
                                                    alt = "mainImage"
                                                }
                                            }
                                        }
                                        //val icon = draft["icon"] as String

                                        TableCell {
                                            if (draft["icon"] != null) {
                                                ReactHTML.img {
                                                    css {
                                                        borderRadius = 8.px
                                                        width = 150.px
                                                        padding = 5.px
                                                    }
                                                    src = draft["icon"]
                                                    alt = "icon"
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            WeaponColumnHeader()
                            TableBody {
                                TableRow {
                                    TableCell {
                                        +editName
                                    }
                                    TableCell {
                                        if (element["mainImage"] != null) {
                                            ReactHTML.img {
                                                css {
                                                    borderRadius = 8.px
                                                    width = 150.px
                                                    padding = 5.px
                                                }
                                                src = element["mainImage"]
                                                alt = "mainImage"
                                            }
                                        }
                                        Input {
                                            type = "file"
                                            onChange = uploadMainImage
                                        }
                                    }
                                    TableCell {
                                        if (element["entireIcon"] != null) {
                                            ReactHTML.img {
                                                css {
                                                    borderRadius = 8.px
                                                    width = 150.px
                                                    padding = 5.px
                                                }
                                                src = element["entireIcon"]
                                                alt = "entireIcon"
                                            }
                                        }
                                        Input {
                                            type = "file"
                                            onChange = uploadEntireIcon
                                        }
                                    }
                                    TableCell {
                                        if (element["brokenIcon"] != null) {
                                            ReactHTML.img {
                                                css {
                                                    borderRadius = 8.px
                                                    width = 150.px
                                                    padding = 5.px
                                                }
                                                src = element["brokenIcon"]
                                                alt = "brokenIcon"
                                            }
                                        }
                                        Input {
                                            type = "file"
                                            onChange = uploadBrokenIcon
                                        }
                                    }
                                    TableCell {
                                        if (draftExists) {

                                            h5 {
                                                css {
                                                    color = Color("#FFA500")
                                                }
                                                +"Warning: uncommited draft will be rewritten on new"
                                            }
                                        }
                                        SubmitLocalButtonBox {
                                            type = props.type
                                            editItem = editName
                                            userId = currentUser
                                            setHasDraft = setDraftExist
                                            mainImage = mainImageFile
                                            entireIcon = entireIconFile
                                            brokenIcon = brokenIconFile
                                        }

                                    }
                                }
                                if (draftExists) {
                                    h3 { +"Have changes:" }

                                    TableRow {

                                        sx {
                                            backgroundColor = Color("#ADD8E6")
                                        }
                                        TableCell {
                                            +draftName
                                        }
                                        TableCell {
                                            if (draft["mainImage"] != null) {
                                                ReactHTML.img {
                                                    css {
                                                        borderRadius = 8.px
                                                        width = 150.px
                                                        padding = 5.px
                                                    }
                                                    src = draft["mainImage"]
                                                    alt = "mainImage"
                                                }
                                            }
                                        }

                                        TableCell {
                                            if (draft["entireIcon"] != null) {
                                                ReactHTML.img {
                                                    css {
                                                        borderRadius = 8.px
                                                        width = 150.px
                                                        padding = 5.px
                                                    }
                                                    src = draft["entireIcon"]
                                                    alt = "entireIcon"
                                                }
                                            }

                                        }
                                        TableCell {
                                            if (draft["brokenIcon"] != null) {
                                                ReactHTML.img {
                                                    css {
                                                        borderRadius = 8.px
                                                        width = 150.px
                                                        padding = 5.px
                                                    }
                                                    src = draft["brokenIcon"]
                                                    alt = "brokenIcon"
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        }

                    }
//                    }

                }
            }
        } else {
            div {
                h2 {
                    css {
                        color = Color("#8B0000")
                    }
                    +"Item does not exists or edit link is wrong"
                }
            }
        }
    }

    div {
        css {
            textAlign = TextAlign.center
            fontWeight = FontWeight.bold
        }
        Link {
            to = "/"
            h3 { +"Go to main page" }
        }
    }
}


val SubmitLocalButtonBox = FC<EditProps> { props ->

    val currentUser = props.userId
    val handleLocalSaveClick = { event: MouseEvent<*, *> ->
        event.preventDefault()
        props.setHasDraft(false)
        sendNewDraftRequest(props, currentUser)
    }
    Box {
        sx {
            gap = Gap.normal
        }

        ButtonStyled {
            action = handleLocalSaveClick
            buttonText = "Local save"
        }
    }
}




