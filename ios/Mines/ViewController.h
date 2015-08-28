//
//  ViewController.h
//  CollectionView
//
//  Created by Volker on 11/14/12.
//  Copyright (c) 2012 Volker. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ViewController : UIViewController
@property (weak, nonatomic) IBOutlet UIView *gameContainer;
@property (weak, nonatomic) IBOutlet UIButton *reloadButton;
- (IBAction)reloadPressed:(id)sender;

@end
