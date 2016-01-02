#import <UIKit/UIKit.h>

@interface UIBoard : UIView

- (UILabel*) createFieldUIWithTag:(int)tag ForX:(int)x AndY:(int)y WithSize:(int)size AndBorder:(int)border;

@end
