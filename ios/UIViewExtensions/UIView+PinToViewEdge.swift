import Foundation
import UIKit

extension UIView {

    @discardableResult public func pinTop(to view: UIView,
                                          constant: CGFloat = 0,
                                          priority: UILayoutPriority = UILayoutPriorityRequired,
                                          relatedBy relation: NSLayoutRelation = .equal) -> NSLayoutConstraint {
        return pin(edge: .top, to: .top, of: view, constant: constant, priority: priority, relatedBy: relation)
    }

    @discardableResult public func pinBottom(to view: UIView,
                                             constant: CGFloat = 0,
                                             priority: UILayoutPriority = UILayoutPriorityRequired,
                                             relatedBy relation: NSLayoutRelation = .equal) -> NSLayoutConstraint {
        return pin(edge: .bottom, to: .bottom, of: view, constant: constant, priority: priority, relatedBy: relation)
    }

    @discardableResult public func pinLeading(to view: UIView,
                                              constant: CGFloat = 0,
                                              priority: UILayoutPriority = UILayoutPriorityRequired,
                                              relatedBy relation: NSLayoutRelation = .equal) -> NSLayoutConstraint {
        return pin(edge: .leading, to: .leading, of: view, constant: constant, priority: priority, relatedBy: relation)
    }

    @discardableResult public func pinTrailing(to view: UIView,
                                               constant: CGFloat = 0,
                                               priority: UILayoutPriority = UILayoutPriorityRequired,
                                               relatedBy relation: NSLayoutRelation = .equal) -> NSLayoutConstraint {
        return pin(edge: .trailing, to: .trailing, of: view,
                   constant: constant, priority: priority, relatedBy: relation)
    }

    @discardableResult public func pinCenterX(to view: UIView,
                                              constant: CGFloat = 0,
                                              priority: UILayoutPriority = UILayoutPriorityRequired,
                                              relatedBy relation: NSLayoutRelation = .equal) -> NSLayoutConstraint {
        return pinView(to: view, constant: constant, priority: priority, edge: .centerX, relatedBy: relation)
    }

    @discardableResult public func pinCenterY(to view: UIView,
                                              constant: CGFloat = 0,
                                              priority: UILayoutPriority = UILayoutPriorityRequired,
                                              multiplier: CGFloat = 1,
                                              relatedBy relation: NSLayoutRelation = .equal) -> NSLayoutConstraint {
        return pinView(to: view, constant: constant, priority: priority,
                       edge: .centerY,
                       multiplier: multiplier,
                       relatedBy: relation)
    }

    @discardableResult public func pinView(to view: UIView,
                                           constant: CGFloat = 0,
                                           priority: UILayoutPriority = UILayoutPriorityRequired,
                                           edge: NSLayoutAttribute,
                                           multiplier: CGFloat = 1,
                                           relatedBy relation: NSLayoutRelation = .equal) -> NSLayoutConstraint {

        translatesAutoresizingMaskIntoConstraints = false
        if view !== self.superview {
            view.translatesAutoresizingMaskIntoConstraints = false
        }

        let constraint = NSLayoutConstraint(item: self,
                                            attribute: edge,
                                            relatedBy: relation,
                                            toItem: view,
                                            attribute: edge,
                                            multiplier: multiplier,
                                            constant: constant)
        constraint.priority = priority
        constraint.isActive = true

        return constraint
    }

    @discardableResult public func pin(edge: Edge,
                                       to otherEdge: Edge,
                                       of view: UIView,
                                       constant: CGFloat = 0,
                                       priority: UILayoutPriority = UILayoutPriorityRequired,
                                       relatedBy relation: NSLayoutRelation = .equal) -> NSLayoutConstraint {
        guard let superview = self.superview else {
            preconditionFailure("view has no superview")
        }

        translatesAutoresizingMaskIntoConstraints = false
        if view !== superview {
            view.translatesAutoresizingMaskIntoConstraints = false
        }

        let constraint = NSLayoutConstraint(item: self,
                                            attribute: edge.layoutAttribute,
                                            relatedBy: relation,
                                            toItem: view,
                                            attribute: otherEdge.layoutAttribute,
                                            multiplier: 1,
                                            constant: constant)
        constraint.priority = priority
        superview.addConstraint(constraint)
        return constraint
    }

    @discardableResult public func addWidthConstraint(
        constant: CGFloat,
        priority: UILayoutPriority = UILayoutPriorityRequired,
        relatedBy relation: NSLayoutRelation = .equal) -> NSLayoutConstraint {

        translatesAutoresizingMaskIntoConstraints = false

        let constraint = NSLayoutConstraint(item: self,
                                            attribute: .width,
                                            relatedBy: relation,
                                            toItem: nil,
                                            attribute: .notAnAttribute,
                                            multiplier: 1,
                                            constant: constant)
        constraint.priority = priority
        addConstraint(constraint)
        return constraint

    }

    @discardableResult public func addHeightConstraint(
        constant: CGFloat,
        priority: UILayoutPriority = UILayoutPriorityRequired,
        relatedBy relation: NSLayoutRelation = .equal) -> NSLayoutConstraint {

        translatesAutoresizingMaskIntoConstraints = false

        let constraint = NSLayoutConstraint(item: self,
                                            attribute: .height,
                                            relatedBy: relation,
                                            toItem: nil,
                                            attribute: .notAnAttribute,
                                            multiplier: 1,
                                            constant: constant)
        constraint.priority = priority
        addConstraint(constraint)
        return constraint
    }

    @discardableResult public func setWidthEqualToWidth(
        of view: UIView,
        priority: UILayoutPriority = UILayoutPriorityRequired) -> NSLayoutConstraint {

        translatesAutoresizingMaskIntoConstraints = false

        let constraint = NSLayoutConstraint(item: self,
                                            attribute: .width,
                                            relatedBy: .equal,
                                            toItem: view,
                                            attribute: .width,
                                            multiplier: 1,
                                            constant: 0)
        constraint.priority = priority
        constraint.isActive = true
        return constraint
    }

    public func addSizeConstraint(size: CGSize) {
        addWidthConstraint(constant: size.width)
        addHeightConstraint(constant: size.height)
    }
}
