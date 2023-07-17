//package frontend.pages.titles
//
//import frontend.components.Header
//import kotlinx.coroutines.MainScope
//import kotlinx.coroutines.launch
//import mui.material.*
//import react.FC
//import react.Props
//import react.dom.html.ReactHTML.h1
//import react.router.useParams
//import react.useEffectOnce
//import react.useState
//
//
//private val scope = MainScope()
//val HeroPage = FC<Props> { props ->
//    val id = useParams()["id"]
//
////    var hero: HeroModel? by useState(null)
//    useEffectOnce {
//        scope.launch {
////            hero = HeroesApi.fetchById(id)
//        }
//    }
//    Header {}
//    Container {
//        h1 {
//            +"Hero: ${id}"
//        }
//    }
//
//
//}
