#import "ViewController.h"
#import "MyGameViewController.h"
#import "de/devisnik/mine/GameFactory.h"
#import "de/devisnik/mine/IStopWatch.h"


@implementation ViewController

NSTimer *gameTimer;
id<DeDevisnikMineIGame> minesGame;

- (void) loadView {
    [super loadView];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(appWillResignActive:) name:UIApplicationWillResignActiveNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(appDidBecomeActive:) name:UIApplicationDidBecomeActiveNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(appWillTerminate:) name:UIApplicationWillTerminateNotification object:nil];
}

-(void)appDidBecomeActive:(NSNotification*)note
{
    gameTimer = [NSTimer scheduledTimerWithTimeInterval:1.0
                                                 target:self selector:@selector(timerTick:)
                                               userInfo:[self userInfo]
                                                repeats:YES];
}

-(void)appWillResignActive:(NSNotification*)note
{
    [gameTimer invalidate];
    gameTimer = nil;
}

-(void)appWillTerminate:(NSNotification*)note
{
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIApplicationDidBecomeActiveNotification object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIApplicationWillResignActiveNotification object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIApplicationWillTerminateNotification object:nil];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    [self createNewGame];
    [self startNewGame];
}

- (void)viewDidDisappear:(BOOL)animated
{
    [super viewDidDisappear:animated];
}

- (NSDictionary *)userInfo {
    return @{ @"StartDate" : [NSDate date] };
}

- (void) createNewGame {
    [minesGame removeListenerWithDeDevisnikMineIMinesGameListener:self];
    [[minesGame getWatch] removeListenerWithDeDevisnikMineIStopWatchListener:self];

    minesGame = [DeDevisnikMineGameFactory createWithInt:10 withInt:15 withInt:15];
    [minesGame addListenerWithDeDevisnikMineIMinesGameListener:self];
    [[minesGame getWatch] addListenerWithDeDevisnikMineIStopWatchListener:self];

    [self displayFlags: [minesGame getBombCount]];
    [self displayTime: [[minesGame getWatch] getTime]];
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
            [(MyGameViewController*)controller startNewGame:minesGame];
    }
}

- (void)timerTick:(NSTimer*)theTimer {
    [minesGame tickWatch];
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
    [self.timeDisplay setText:[NSString stringWithFormat:@"%.3d", time]];
}

- (void)displayFlags:(int)flags {
    [self.bombsDisplay setText:[NSString stringWithFormat:@"%.3d", flags]];
}

@end
