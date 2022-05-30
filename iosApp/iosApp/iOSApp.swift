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
    
    deinit {
        stateWatcher?.close()
        sideEffectWatcher?.close()
    }
}
