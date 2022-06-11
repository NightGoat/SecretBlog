import SwiftUI
import shared

@main
class iOSApp: App {
    let viewModel: StoreViewModel
    let store: ObservableStore
    
    required init() {
        KoinKt.doInitKoin()
        viewModel = StoreViewModel()
        store = ObservableStore(store: viewModel)
    }
    
	var body: some Scene {
		WindowGroup {
            ContentView().environmentObject(store)
		}
	}
}


class ObservableStore: ObservableObject {
    @Published public var state: AppState =  AppState(blogMessages: [], secretBlogsState: SecretBlogsState.hidden, isEdit: false)
    
    @Published public var sideEffect: BlogEffect?
    
    let store: StoreViewModel
    
    var stateWatcher : Closeable?
    var sideEffectWatcher : Closeable?
    
    init(store: StoreViewModel) {
        self.store = store
        stateWatcher = self.store.watchState().watch { [weak self] state in
            self?.state = state
        }
        sideEffectWatcher = self.store.watchSideEffect().watch { [weak self] state in
            self?.sideEffect = state
        }
    }
    
    public func dispatch(_ action: BlogAction) {
        store.dispatch(action: action)
    }
    
    public func addMessage(message: String, isSecret: Bool) {
        store.addMessage(message: message, isSecret: isSecret)
    }
    
    deinit {
        stateWatcher?.close()
        sideEffectWatcher?.close()
    }
}

public typealias DispatchFunction = (BlogAction) -> ()

public protocol ConnectedView: View {
    associatedtype Props
    associatedtype V: View
    
    func map(state: AppState, dispatch: @escaping DispatchFunction) -> Props
    func body(props: Props) -> V
}

public extension ConnectedView {
    func render(state: AppState, dispatch: @escaping DispatchFunction) -> V {
        let props = map(state: state, dispatch: dispatch)
        return body(props: props)
    }
    
    var body: StoreConnector<V> {
        return StoreConnector(content: render)
    }
}

public struct StoreConnector<V: View>: View {
    @EnvironmentObject var store: ObservableStore
    let content: (AppState, @escaping DispatchFunction) -> V
    
    public var body: V {
        return content(store.state, store.dispatch)
    }
}
