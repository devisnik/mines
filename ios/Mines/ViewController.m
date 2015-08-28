//  ViewController.m
//  CollectionView
//
//  Created by Volker on 11/14/12.
//  Copyright (c) 2012 Volker. All rights reserved.
//

#import "ViewController.h"
#import "MyGameViewController.h"

@interface ViewController ()

@end

@implementation ViewController

- (void) loadView {
    [super loadView];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)reloadPressed:(id)sender {
    NSLog(@"reload triggered");
    NSArray *childControllers = [self childViewControllers];
    for (UIViewController *controller in childControllers) {
        NSLog(@"%@", [controller description]);
        if ([controller isKindOfClass:[MyGameViewController class]])
            [(MyGameViewController*)controller startNewGame];
    }
}

@end
