#import <UIKit/UIKit.h>
#import "IGame.h"
#import "IMinesGameListener.h"
#import "IStopWatchlistener.h"

@interface ViewController : UIViewController<DeDevisnikMineIMinesGameListener, DeDevisnikMineIStopWatchListener>

@property (weak, nonatomic) IBOutlet UILabel *bombsDisplay;
@property (weak, nonatomic) IBOutlet UILabel *timeDisplay;
@property (weak, nonatomic) IBOutlet UIButton *reloadButton;

- (IBAction)reloadPressed:(id)sender;

@end
