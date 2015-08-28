//
//  UIBoard.h
//  CollectionView
//
//  Created by Volker on 11/19/12.
//  Copyright (c) 2012 Volker. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIBoard : UIView

- (UILabel*) createFieldUIWithTag:(int)tag ForX:(int)x AndY:(int)y WithSize:(int)size AndBorder:(int)border;
@end
