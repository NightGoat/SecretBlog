import SwiftUI
import shared

struct ContentView: View {
    @EnvironmentObject var store: ObservableStore
    
    @SwiftUI.State var messages = ["Hello!", "How do you do?"]
    @SwiftUI.State var text = ""
    
    var body: some View {
        VStack {
            messagesView()
            textInputView()
        }
    }
        
    func messagesView() -> some View {
        ScrollView {
            ScrollViewReader { value in
                ForEach(messages, id: \.self) { message in
                    Text(message)
                        .padding()
                        .overlay(
                            RoundedRectangle(cornerRadius: 16)
                                .stroke(Color.blue, lineWidth: 4)
                        )
                        .padding()
                        .frame(maxWidth: .infinity, alignment: .trailing)
                }.onChange(of: text) { _ in
                    value.scrollTo(text)
                }
            }
        }.onReceive(store.$state) { value in
            self.messages = value.visibleMessages.map { message in
                message.text
            }
        }
    }

    
    func textInputView() -> some View {
        HStack {
            TextField("Message...", text: $text)
            Button(action: {
                messages.append(text)
                text = ""
            }, label: {
                Image(systemName: "paperplane.fill")
            })
        }.padding()
    }
	
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
