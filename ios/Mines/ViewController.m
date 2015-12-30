#import "ViewController.h"
#import "MyGameViewController.h"
#import "de/devisnik/mine/GameFactory.h"
#import "de/devisnik/mine/IStopWatch.h"

@interface ViewController ()

@end

@implementation ViewController

- (void) loadView {
    [super loadView];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    
    [NSTimer scheduledTimerWithTimeInterval:1.0
                                     target:self selector:@selector(timerTick:)
                                   userInfo:[self userInfo] repeats:YES];

    [self createNewGame];
    [self startNewGame];
}

- (NSDictionary *)userInfo {
    return @{ @"StartDate" : [NSDate date] };
}

- (void) createNewGame {
    if (self.minesGame != nil) {
        [self.minesGame removeListenerWithDeDevisnikMineIMinesGameListener:self];
        [[self.minesGame getWatch] removeListenerWithDeDevisnikMineIStopWatchListener:self];
    }
    self.minesGame = [DeDevisnikMineGameFactory createWithInt:10 withInt:15 withInt:15];
    [self.minesGame addListenerWithDeDevisnikMineIMinesGameListener:self];
    [[self.minesGame getWatch] addListenerWithDeDevisnikMineIStopWatchListener:self];

    [self displayFlags: [self.minesGame getBombCount]];
    [self displayTime: [[self.minesGame getWatch] getTime]];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)reloadPressed:(id)sender {
    NSLog(@"reload triggered");

    [self createNewGame];
    [self startNewGame];
}

-(void) startNewGame {
    NSArray *childControllers = [self childViewControllers];
    for (UIViewController *controller in childControllers) {
        NSLog(@"%@", [controller description]);
        if ([controller isKindOfClass:[MyGameViewController class]])
            [(MyGameViewController*)controller startNewGame:self.minesGame];
    }
}

- (void)timerTick:(NSTimer*)theTimer {
    [self.minesGame tickWatch];
}

- (void)onDisarmed {
    
}

- (void)onBusted {
    
}

- (void)onChangeWithInt:(jint)flags
                withInt:(jint)mines {
    [self displayFlags: mines - flags];
}

- (void)onStart {
    
}

- (void)onTimeChangeWithInt:(jint)currentTime {
    [self displayTime: currentTime];
}

- (void)displayTime:(int)time {
    [self.timeDisplay setText:[NSString stringWithFormat:@"%d", time]];
}

- (void)displayFlags:(int)flags {
    [self.bombsDisplay setText:[NSString stringWithFormat:@"%d", flags]];
}


@end
