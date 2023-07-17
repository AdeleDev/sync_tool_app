//package frontend
//
//import common.HeroesPaths
//import common.RootPaths
//import frontend.pages.IndexPage
//import frontend.pages.titles.HeroNewPage
//import frontend.pages.titles.HeroPage
//import frontend.pages.titles.HeroesIndexPage
//import react.FC
//import react.Props
//import react.createElement
//import react.router.dom.BrowserRouter
//import react.router.Routes
//import react.router.Route
//
//val App = FC<Props> {
//
//    BrowserRouter {
//        Routes {
//            Route {
//                index = true
//                element = createElement(IndexPage)
//            }
//            Route {
//                path = RootPaths().Heroes.toString()
//                element = createElement(HeroesIndexPage)
//            }
//            Route {
//                path = RootPaths().Heroes.New.toString()
//                element = createElement(HeroNewPage)
//            }
//            Route {
//                path = RootPaths().Heroes.Id("/:id").toString()
//                element = createElement(HeroPage)
//            }
////            Route {
////                path = RootPaths().Commit.toString()
////                element = createElement(CommitPage)
////            }
//        }
//    }
//}