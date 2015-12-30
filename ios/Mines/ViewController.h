#import <UIKit/UIKit.h>
#import "de/devisnik/mine/IGame.h"
#import "de/devisnik/mine/IMinesGameListener.h"
#import "de/devisnik/mine/IStopWatchlistener.h"

@interface ViewController : UIViewController<DeDevisnikMineIMinesGameListener, DeDevisnikMineIStopWatchListener>

@property (weak, nonatomic) IBOutlet UILabel *bombsDisplay;
@property (weak, nonatomic) IBOutlet UILabel *timeDisplay;
@property (weak, nonatomic) IBOutlet UIButton *reloadButton;
- (IBAction)reloadPressed:(id)sender;

@property(nonatomic, retain) id<DeDevisnikMineIGame> minesGame;

@end
