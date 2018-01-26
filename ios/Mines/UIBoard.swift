//
//  UIBoard.swift
//  Mines
//
//  Created by Tobias Heine on 26.01.18.
//  Copyright © 2018 devisnik. All rights reserved.
//

import UIKit

@objc class UIBoard: UIView {
    
    public func createFieldUI(withTag:Int, for x:Int, and y:Int, with size:Int, and border:Int) -> UIImageView {
        let rect = CGRect(x: x*(size+2*border)+border, y: y*(size+2*border)+border, width: size, height: size)
        let field = UIImageView(frame: rect)
        field.isUserInteractionEnabled = true
        field.backgroundColor = UIColor.white
        field.image = UIImage(named: "image__01")
        field.tag = withTag

        addSubview(field)
        
        return field
    }
}

