#import <UIKit/UIKit.h>
#import "de/devisnik/mine/IFieldListener.h"
#import "de/devisnik/mine/IGame.h"
#import "de/devisnik/mine/IBoard.h"
#import "UIBoard.h"

@interface MyGameViewController : UIViewController<UIScrollViewDelegate>

@property(nonatomic, retain) id<DeDevisnikMineIGame> minesGame;
@property(nonatomic, retain) id<DeDevisnikMineIBoard> minesBoard;

@property (weak, nonatomic) IBOutlet UIBoard *boardUI;
@property (strong, nonatomic) IBOutlet UIScrollView *scrollContainer;

-(void) startNewGame;
-(void) updateUIForField:(id<DeDevisnikMineIField>)field;
@end
