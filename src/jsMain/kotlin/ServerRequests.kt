import components.tables.ELEMENT_NOT_EXIST_VALUE
import components.tables.EditProps
import kotlinx.browser.window
import kotlinx.js.Object
import kotlinx.js.jso
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import org.w3c.xhr.FormData
import react.StateSetter
import react.useEffect
import kotlin.js.Promise
import kotlin.js.json

fun fetchMainData(type: String, setData: StateSetter<Array<String>>, elementCounter: Int) {
    useEffect(elementCounter) {
        window.fetch(BASE_URL + type).then {
            it.json().then { any -> setData(any.unsafeCast<Array<String>>()) }
        }
    }
}

fun fetchImageData(type: String, names: Array<String>, setData: StateSetter<Array<dynamic>>) {
    useEffect(names) {
        console.log("Effect for image $type")
        var promises: Array<Promise<Any>> = arrayOf()
        for (name in names) {
            promises += window.fetch("$BASE_URL$type/$name").then { response -> response.formData() }
        }
        var data: Array<dynamic> = arrayOf()
        Promise.all(promises).then { responses ->
            console.log(responses)
            responses.map { response ->
                console.log(response)
                response as FormData
            }.map { formData ->
                console.log(formData)
                formRequestData(formData)
            }.forEach { e ->
                data += e as Object
            }
            setData(data)
        }
    }
}

fun fetchAllDraftData(
    type: String,
    existedElements: Array<String>,
    userId: String,
    setData: StateSetter<Array<dynamic>>
) {
    useEffect(existedElements) {
        console.log("Effect for drafts $type")
        var promises: Array<Promise<Any?>> = arrayOf()
        for (i in existedElements.indices) {
            promises += window.fetch("$BASE_URL$type/${existedElements[i]}/${getStubUserId(userId)}")
                .then { response ->
                    console.log(response)
                    response.formData()
                }
                .catch { error ->
                    console.log(error)
                    null
                }
        }
        var data: Array<dynamic> = arrayOf()
        Promise.all(promises).then { responses ->
            console.log("Drafts responses")
            console.log(responses)
            responses.filterNotNull().map { response ->
                response as FormData
            }.map { formData ->
                formRequestData(formData)
            }.forEach { e ->
                data += e as Object
            }
            setData(data)
        }
    }
}

fun newItemPostRequest(name: String, type: String, addNewElement: StateSetter<Int>, size: Int) {
    val form = FormData()
    form.append("name", name)

    Promise.resolve(window.fetch(
        BASE_URL + type, jso {
            method = "POST"
            body = form
            headers = json(
                "Accept" to "application/json",
                "pragma" to "no-cache"
            )
        }
    )).then {
        addNewElement(size + 1)
    }
}


fun fetchEditData(type: String, name: String, setData: StateSetter<dynamic>) {
    useEffect(*emptyArray()) {
        window.fetch("$BASE_URL$type/$name").then {
            it.formData().then { formData ->
                val element = formRequestData(formData)
                setData(element)
            }.catch {
                val failureData = "{'name': '$ELEMENT_NOT_EXIST_VALUE', 'mainImage':'', 'icon':''}"
                setData(js(failureData))
            }
        }
    }
}

fun fetchEditDraftData(
    type: String,
    name: String,
    userId: String,
    setData: StateSetter<dynamic>,
    draftExist: Boolean,
    setDraftExist: StateSetter<Boolean>
) {
    useEffect(draftExist) {
        window.fetch("$BASE_URL$type/$name/${getStubUserId(userId)}").then {
            it.formData().then { formData ->
                val element = formRequestData(formData)
                setDraftExist(true)
                setData(element)
            }
        }
    }
}


fun submitDraftRequest(name: String, type: String, userId: String) {
    val element: dynamic = js("{}")
    element["name"] = name
    element["user_id"] = getStubUserId(userId)
    Promise.resolve(window.fetch("$BASE_URL$type", jso {
        method = "PUT"
        body = JSON.stringify(element)
        headers = json(
            "Content-Type" to "application/json",
            "Accept" to "application/json",
            "pragma" to "no-cache"
        )
    }
    ))
}

fun deleteDraftRequest(name: String, type: String, userId: String) {
    Promise.resolve(window.fetch("$BASE_URL$type/$name/${getStubUserId(userId)}", jso {
        method = "DELETE"
        headers = json(
            "Accept" to "application/json",
            "pragma" to "no-cache"
        )
    }
    ))
}

fun sendNewDraftRequest(props: EditProps, userId: String) {
    val form = FormData()
    form.append("name", props.editItem)
    if (props.mainImage != null) {
        form.append(
            "mainImage",
            props.mainImage!!,
            props.mainImage!!.name
        )
    }
    if (props.icon != null) {
        form.append(
            "icon",
            props.icon!!,
            props.icon!!.name
        )
    }
    if (props.entireIcon != null) {
        form.append(
            "entireIcon",
            props.entireIcon!!,
            props.entireIcon!!.name
        )
    }
    if (props.brokenIcon != null) {
        form.append(
            "brokenIcon",
            props.brokenIcon!!,
            props.brokenIcon!!.name
        )
    }

    Promise.resolve(window.fetch("$BASE_URL${props.type}/${getStubUserId(userId)}", jso {
        method = "PUT"
        body = form
        headers = json(
            "Accept" to "application/json",
            "pragma" to "no-cache"
        )
    }
    )).then {
        props.setHasDraft(true)
    }
}

private fun formRequestData(formData: FormData): dynamic {
    val element: dynamic = js("{}")
    element["name"] = formData.get("name")
    if (formData.has("mainImage")) {
        element["mainImage"] = URL.createObjectURL(formData.get("mainImage") as Blob)
    }
    if (formData.has("icon")) {
        element["icon"] = URL.createObjectURL(formData.get("icon") as Blob)
    }
    if (formData.has("entireIcon")) {
        element["entireIcon"] = URL.createObjectURL(formData.get("entireIcon") as Blob)
    }
    if (formData.has("brokenIcon")) {
        element["brokenIcon"] = URL.createObjectURL(formData.get("brokenIcon") as Blob)
    }
    return element
}



