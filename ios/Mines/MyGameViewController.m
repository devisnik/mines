//
//  MyGameViewController.m
//  CollectionView
//
//  Created by Volker on 11/15/12.
//  Copyright (c) 2012 Volker. All rights reserved.
//

#import "MyGameViewController.h"
#import "IGame.h"
#import "IBoard.h"
#import "IField.h"
#import "GameFactory.h"
#import "Point.h"
#import "UIBoard.h"

@interface MyGameViewController ()

@end

@implementation MyGameViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self logSize];
//    [self startNewGame];
}

-(void)logSize {
    NSLog(@"%f, %f", self.boardUI.bounds.size.width, self.boardUI.bounds.size.height);
}

-(void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [self logSize];
}

-(void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    [self logSize];
    [self startNewGame];
}

- (void) startNewGame {
    BOOL firstTime = YES;
    if (self.minesBoard != nil) {
        [self unlistenForFieldChanges];
        firstTime = NO;
    }
    self.minesGame = [DeDevisnikMineGameFactory createWithInt:10 withInt:15 withInt:15];
    self.minesBoard = [self.minesGame getBoard];
    if (firstTime)
        [self createUI];
    [self reloadUI];
    [self listenForFieldChanges];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)createUI
{
    DeDevisnikMinePoint *dimension = [self.minesBoard getDimension];
    int border = 1;
    int size = (self.boardUI.bounds.size.width-2*border*dimension->x_) / dimension->x_;

    [self.boardUI setFrame:CGRectMake(0, 0, (size+2*border)*dimension->x_, (size+2*border)*dimension->y_)];
    
    for (int x = 0; x < dimension->x_; x++) {
        for (int y = 0; y< dimension->y_; y++) {
            UIView* fieldUI =[[self boardUI] createFieldUIWithTag:x*dimension->y_+y ForX:x AndY:y WithSize:size AndBorder:border];
            UILongPressGestureRecognizer *longPress = [[UILongPressGestureRecognizer alloc] initWithTarget:self action:@selector(longPressTap:)];
            UITapGestureRecognizer *press = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(pressTap:)];
            [fieldUI addGestureRecognizer:longPress];
            [fieldUI addGestureRecognizer:press];
        }
    }
    
    NSLog(@"nach createUI: %f, %f", self.boardUI.bounds.size.width, self.boardUI.bounds.size.height);
    self.scrollContainer.contentSize = CGSizeMake(self.boardUI.bounds.size.width, self.boardUI.bounds.size.height);
}

- (void)reloadUI
{
    DeDevisnikMinePoint *dimension = [self.minesBoard getDimension];
    for (int x = 0; x < dimension->x_; x++) {
        for (int y = 0; y< dimension->y_; y++) {
            [self updateUIForField:[self.minesBoard getFieldWithInt:x withInt:y]];
        }
    }
}

- (void)listenForFieldChanges
{
    DeDevisnikMinePoint *dimension = [self.minesBoard getDimension];
    for (int x = 0; x < dimension->x_; x++) {
        for (int y = 0; y< dimension->y_; y++) {
            [[self.minesBoard getFieldWithInt:x withInt:y] addListenerWithDeDevisnikMineIFieldListener:(id<DeDevisnikMineIFieldListener>)self];
        }
    }
}

- (void)unlistenForFieldChanges
{
    DeDevisnikMinePoint *dimension = [self.minesBoard getDimension];
    for (int x = 0; x < dimension->x_; x++) {
        for (int y = 0; y< dimension->y_; y++) {
            [[self.minesBoard getFieldWithInt:x withInt:y] removeListenerWithDeDevisnikMineIFieldListener:(id<DeDevisnikMineIFieldListener>)self];
        }
    }
}

- (id<DeDevisnikMineIField>) getFieldForIndex:(int)index {
    DeDevisnikMinePoint *dimension = self.minesBoard.getDimension;
    int x = index/dimension->y_;
    int y = index%dimension->y_;
    return [self.minesBoard getFieldWithInt:x withInt:y];
}

- (void) pressTap:(UITapGestureRecognizer*)gesture {
//    NSLog(@"Tap");
    if (gesture.state == UIGestureRecognizerStateEnded) {
//        NSLog(@"Click");
        long index = gesture.view.tag;
        id<DeDevisnikMineIField>field = [self getFieldForIndex:(int)index];
        [self.minesGame onRequestFlagWithDeDevisnikMineIField: field];
    }
}

- (void)longPressTap:(UILongPressGestureRecognizer*)gesture {
//    NSLog(@"Long Press");
    if ( gesture.state == UIGestureRecognizerStateBegan ) {
        long index = gesture.view.tag;
        id<DeDevisnikMineIField>field = [self getFieldForIndex:(int)index];
        [self.minesGame onRequestOpenWithDeDevisnikMineIField: field];
    }
}

- (void) updateUIForField:(id<DeDevisnikMineIField>)field {
//    DeDevisnikMinePoint *fpoint = [self.minesBoard getPositionWithDeDevisnikMineIField:field];
//    NSLog(@"%i, %i", fpoint.x, fpoint.y);
    DeDevisnikMinePoint *dimension = self.minesBoard.getDimension;
    DeDevisnikMinePoint *point = [self.minesBoard getPositionWithDeDevisnikMineIField:field];
    int index = point->x_*dimension->y_+point->y_;
    UIImageView* fieldUI =(UIImageView*)[[self.boardUI subviews] objectAtIndex:index];
    [self updateView:fieldUI ForField:field];
}

-(void) updateView:(UIImageView*)fieldUI ForField:(id<DeDevisnikMineIField>)field {
    NSString *imageId = [NSString stringWithFormat:@"dark_image_%02d", [field getImage]];
    [fieldUI setImage: [UIImage imageNamed:imageId]];
}


- (void)onFieldOpenChangeWithDeDevisnikMineIField:(id<DeDevisnikMineIField>)field
                                         withBoolean:(jboolean)value {
    [self updateUIForField:field];
}

- (void)onFieldFlagChangeWithDeDevisnikMineIField:(id<DeDevisnikMineIField>)field
                                         withBoolean:(jboolean)value {
    [self updateUIForField:field];
}

- (void)onFieldExplodedChangeWithDeDevisnikMineIField:(id<DeDevisnikMineIField>)field
                                             withBoolean:(jboolean)value {
    [self updateUIForField:field];
}

- (void)onFieldTouchedChangeWithDeDevisnikMineIField:(id<DeDevisnikMineIField>)field
                                            withBoolean:(jboolean)value {
    [self updateUIForField:field];
}

- (UIView*)viewForZoomingInScrollView:(UIScrollView *)scrollView {
    return self.boardUI;
}

@end
