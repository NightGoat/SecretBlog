import SwiftUI
import shared

struct ContentView: ConnectedView {
    @EnvironmentObject var store: ObservableStore
    
    struct Props {
        let items: [BlogMessage]
        let onStart: () -> Void
        let onAddMessage: (String, Bool) -> Void
        let onReverseMessages: () -> Void
        let onSelectMessage: (BlogMessage, Bool) -> Void
    }
    
    
    func map(state: AppState, dispatch: @escaping DispatchFunction) -> Props {
        return Props(
            items: state.visibleMessages,
            onStart: {
                dispatch(BlogAction.Start())
            },
            onAddMessage: { text, isSecret in
                dispatch(BlogAction.AddMessage(message: text, isSecret: isSecret))
            },
            onReverseMessages: {
                dispatch(BlogAction.ReverseSecretBlogsVisibility())
            },
            onSelectMessage: { message, isSelected in
                dispatch(BlogAction.SelectMessage(message: message, isSelected: isSelected))
            })
    }
    
    @SwiftUI.State private var isSecretMessagesVisible = false
    @SwiftUI.State private var text = ""
    
    func body(props: Props) -> some View {
        NavigationView {
            VStack {
                messagesView(props: props)
                textInputView(props: props)
            }
            .navigationTitle("Secret Blog")
            .toolbar {
                ToolbarItem(placement: .primaryAction) {
                    HStack {
                        Button("Clear") {
                            store.store.clearDB()
                        }
                        Button("Reverse") {
                            store.store.reverseVisibility()
                        }
                    }
                }
            }
        }
        
    }
    
    func messagesView(props: Props) -> some View {
        ScrollView {
            ScrollViewReader { value in
                ForEach(props.items, id: \.self) { message in
                    Text(message.text)
                        .padding()
                        .overlay(
                            RoundedRectangle(cornerRadius: 8)
                                .stroke(getColor(message: message), lineWidth: 2)
                        )
                        .padding(EdgeInsets(top: 10, leading: 6, bottom: 0, trailing: 10))
                        .frame(maxWidth: .infinity, alignment: .trailing)
                }.onChange(of: text) { _ in
                    value.scrollTo(text)
                }
            }
        }.onAppear {
            props.onStart()
        }
    }
    
    func getColor(message: BlogMessage) -> Color {
        var color = Color.blue
        if message.isSecret {
            color = Color.red
        }
        return color
    }
    
    
    func textInputView(props: Props) -> some View {
        HStack {
            TextField("Message...", text: $text)
            Button(action: {
                props.onAddMessage(text, true)
                text = ""
            }, label: {
                Image(systemName: "paperplane.fill")
            })
            Button(action: {
                props.onAddMessage(text, false)
                text = ""
            }, label: {
                Image(systemName: "paperplane")
            })
        }.padding()
    }
    
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
