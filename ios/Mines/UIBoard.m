#import "UIBoard.h"

@implementation UIBoard

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    return self;
}

- (UIImageView*) createFieldUIWithTag:(int)tag ForX:(int)x AndY:(int)y WithSize:(int)size AndBorder:(int)border {
    CGRect  viewRect = CGRectMake(x*(size+2*border)+border, y*(size+2*border)+border, size, size);
    UIImageView* myView = [[UIImageView alloc] initWithFrame:viewRect];
    [myView setUserInteractionEnabled:YES];
    [myView setBackgroundColor:UIColor.whiteColor];
    [myView setImage: [UIImage imageNamed:@"image__01"]];
    [myView setTag: tag];

    [self addSubview:myView];
    return myView;
}

@end
