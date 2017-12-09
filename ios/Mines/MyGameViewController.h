#import <UIKit/UIKit.h>
#import "IFieldListener.h"
#import "IGame.h"
#import "IBoard.h"
#import "UIBoard.h"

@interface MyGameViewController : UIViewController<UIScrollViewDelegate>

@property(nonatomic, retain) id<DeDevisnikMineIGame> minesGame;
@property(nonatomic, retain) id<DeDevisnikMineIBoard> minesBoard;

@property (weak, nonatomic) IBOutlet UIBoard *boardUI;
@property (strong, nonatomic) IBOutlet UIScrollView *scrollContainer;

-(void) startNewGame:(id<DeDevisnikMineIGame>) game;
-(void) updateUIForField:(id<DeDevisnikMineIField>)field;

@end
