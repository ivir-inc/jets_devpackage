/*
 * Copyright 2026 IVIR Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ivir.devpackage.gui.wrapper;

import org.apache.pivot.wtk.HorizontalAlignment;
import org.apache.pivot.wtk.ImageView;
import org.apache.pivot.wtk.VerticalAlignment;
import org.apache.pivot.wtk.media.Image;

import java.awt.*;

public class ImageViewExt extends ComponentExt<ImageView> {
    ImageView myImageView = new ImageView();

    public ImageViewExt(){}
    public ImageViewExt(Image image){
        setImage(image);
    }

    public void setAsynchronous(boolean asynchronous){
        myImageView.setAsynchronous(asynchronous);
    }
    public void setImage(java.lang.String imageName){
        myImageView.setImage(imageName);
    }
    public void setImage(java.net.URL imageURL){
        myImageView.setImage(imageURL);
    }
    public void setImage(Image image){
        myImageView.setImage(image);
    }
    public void setOpacity(double opacity){
        myImageView.getStyles().put("opacity", opacity);
    }
    public void setBackgroundColor(Color color){
        myImageView.getStyles().put("backgroundColor", color);
    }
    public void setFIll(boolean fIll){
        myImageView.getStyles().put("fill", fIll);
    }
    public void setVerticalAlignment(VerticalAlignment verticalAlignment){
        myImageView.getStyles().put("verticalAlignment", verticalAlignment);
    }
    public void setHorizontalAlignment(HorizontalAlignment horizontalAlignment){
        myImageView.getStyles().put("horizontalAlignment", horizontalAlignment);
    }
    public void setPreserveAspectRatio(boolean preserveAspectRatio){
        myImageView.getStyles().put("preserveAspectRatio", preserveAspectRatio);
    }
    public void load(java.lang.Object context){
        myImageView.load(context);
    }

    @Override
    public ImageView getComponent() {
        return myImageView;
    }
}
