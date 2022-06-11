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
        let onClearMessages: () -> Void
        let onDeleteMessage: ([BlogMessage]) -> Void
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
                isSecretMessagesVisible = !isSecretMessagesVisible
            },
            onSelectMessage: { message, isSelected in
                dispatch(BlogAction.SelectMessage(message: message, isSelected: isSelected))
            },
            onClearMessages: {
                dispatch(BlogAction.ClearDB())
            },
            onDeleteMessage: { messages in
                dispatch(BlogAction.RemoveMessages(messages: messages))
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
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .primaryAction) {
                    HStack {
                        Button( action: {
                            props.onClearMessages()
                        }, label: {
                            Image(systemName: "xmark.bin")
                        })
                        Button( action :{
                            props.onReverseMessages()
                        }, label: {
                            Image(systemName: getEyeIcon())
                        })
                    }
                }
            }
        }
    }
    
    func getEyeIcon() -> String {
        var label = "eye"
        if isSecretMessagesVisible {
            label = "eye.slash"
        }
        return label
    }
    
    func messagesView(props: Props) -> some View {
        ScrollViewReader { value in
            ScrollView {
                ForEach(props.items, id: \.self) { message in
                    Text(message.text)
                        .padding()
                        .foregroundColor(getTextColor(message: message))
                        .background(
                            getMessageBackGroundColor(message: message)
                        )
                        .cornerRadius(8)
                        .padding(EdgeInsets(top: 10, leading: 6, bottom: 0, trailing: 10))
                        .frame(maxWidth: .infinity, alignment: .trailing)
                        .id(message.id)
                }.onChange(of: props.items) { _ in
                    value.scrollTo(props.items.last?.id)
                }.onAppear {
                    value.scrollTo(props.items.last?.id)
                }
            }
        }
        .background(Color(.sRGB, red: 150/255, green: 150/255, blue: 150/255, opacity: 0.1))
        .onAppear {
            props.onStart()
        }
    }
    
    func getMessageBackGroundColor(message: BlogMessage) -> Color {
        var color = Color.white
        if message.isSecret {
            color = Color.accentColor
        }
        return color
    }
    
    func getTextColor(message: BlogMessage) -> Color {
        var color = Color.black
        if message.isSecret {
            color = Color.white
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
            }).padding(4)
            Button(action: {
                props.onAddMessage(text, false)
                text = ""
            }, label: {
                Image(systemName: "paperplane")
            }).padding(4)
        }.padding(EdgeInsets(top: 8, leading: 16, bottom: 2, trailing: 16))
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
