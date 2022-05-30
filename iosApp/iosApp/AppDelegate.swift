//
//  AppDelegate.swift
//  iosApp
//
//  Created by Nightgoat on 24.05.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import UIKit
import Foundation
import shared

class AppDelegate: UIResponder, UIApplicationDelegate {

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        KoinKt.doInitKoin()
        return true
    }
}
