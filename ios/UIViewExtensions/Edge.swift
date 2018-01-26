import Foundation
import UIKit

public enum Edge {
    case top
    case bottom
    case leading
    case trailing

    var layoutAttribute: NSLayoutAttribute {
        switch self {
        case .top:
            return .top
        case .bottom:
            return .bottom
        case .leading:
            return .leading
        case .trailing:
            return .trailing
        }
    }
}
