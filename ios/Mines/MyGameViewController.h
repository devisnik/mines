//
//  MyGameViewController.h
//  CollectionView
//
//  Created by Volker on 11/15/12.
//  Copyright (c) 2012 Volker. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "IFieldListener.h"
#import "IGame.h"
#import "IBoard.h"
#import "UIBoard.h"

@interface MyGameViewController : UIViewController<DeDevisnikMineIFieldListener, UIScrollViewDelegate>

@property(nonatomic, retain) id<DeDevisnikMineIGame> minesGame;
@property(nonatomic, retain) id<DeDevisnikMineIBoard> minesBoard;
@property (weak, nonatomic) IBOutlet UIBoard *boardUI;
@property (strong, nonatomic) IBOutlet UIScrollView *scrollContainer;

-(void) startNewGame;
@end
